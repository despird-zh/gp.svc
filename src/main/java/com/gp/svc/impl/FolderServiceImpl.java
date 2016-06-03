package com.gp.svc.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.acl.Ace;
import com.gp.acl.Acl;
import com.gp.common.Cabinets;
import com.gp.common.FlatColumns;
import com.gp.common.GeneralConstants;
import com.gp.common.IdKey;
import com.gp.common.ServiceContext;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.CabAceDAO;
import com.gp.dao.CabAclDAO;
import com.gp.dao.CabFileDAO;
import com.gp.dao.CabFolderDAO;
import com.gp.dao.CabinetDAO;
import com.gp.dao.PseudoDAO;
import com.gp.exception.ServiceException;
import com.gp.info.CabAceInfo;
import com.gp.info.CabAclInfo;
import com.gp.info.CabFileInfo;
import com.gp.info.CabFolderInfo;
import com.gp.info.CabinetInfo;
import com.gp.info.InfoId;
import com.gp.svc.FolderService;
import com.gp.svc.CommonService;

@Service("folderService")
public class FolderServiceImpl implements FolderService{

	static Logger LOGGER = LoggerFactory.getLogger(FolderServiceImpl.class);

	@Autowired 
	CabFolderDAO cabfolderdao;

	@Autowired 
	CabFileDAO cabfiledao;
	
	@Autowired
	PseudoDAO pseudodao;
	
	@Autowired
	CabAclDAO cabacldao;
	
	@Autowired
	CabAceDAO cabacedao;
	
	@Autowired
	CabinetDAO cabinetdao;
	
	@Autowired
	private CommonService idservice;

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public InfoId<Long> newFolder(ServiceContext<?> svcctx, InfoId<Long> parentkey, CabFolderInfo folder, Acl acl)
			throws ServiceException {
		
		if(InfoId.isValid(parentkey))
			folder.setParentId(parentkey.getId());
		else
			folder.setParentId(GeneralConstants.FOLDER_ROOT);
		
		InfoId<Long> fkey = null;
		// info key not set yet, create a new one and set it 
		if(!InfoId.isValid(folder.getInfoId())){
			
			fkey = idservice.generateId(IdKey.CAB_FOLDER, Long.class);
			folder.setInfoId(fkey);
		}
		try{
			svcctx.setTraceInfo(folder);
			// create folder record
			cabfolderdao.create(folder);
			// set acl setting
			addAcl(svcctx, folder.getInfoId(), acl);
		}catch(DataAccessException dae){
			
			throw new ServiceException(dae);
		}
		return fkey;
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public InfoId<Long> copyFolder(ServiceContext<?> svcctx, InfoId<Long> folderkey, InfoId<Long> destinationPkey)
			throws ServiceException {
		
		CabFolderInfo cfi = null;
		
		try{
			cfi = cabfolderdao.query(folderkey);
			// new folder key
			InfoId<Long> fkey = idservice.generateId(IdKey.CAB_FOLDER, Long.class);
			cfi.setInfoId(fkey);
			cfi.setParentId(destinationPkey.getId());
			// move the current folder 
			cabfolderdao.create(cfi);
			
			// find child sub folders
			List<CabFolderInfo> flist = cabfolderdao.queryByParent(folderkey.getId());
			for(CabFolderInfo finfo : flist){
				// recursively copy folder to target location
				copyFolder(svcctx, finfo.getInfoId(), fkey);
			}
			// find child files 
			List<CabFileInfo> filelist = cabfiledao.queryByParent(fkey.getId());
			for(CabFileInfo fileinfo : filelist){
				// recursively copy file to target location
				InfoId<Long> filekey = idservice.generateId(IdKey.CAB_FILE, Long.class);
				fileinfo.setInfoId(filekey);
				fileinfo.setParentId(fkey.getId());
				cabfiledao.create(fileinfo);
			}
			
		}catch(DataAccessException dae){
			throw new ServiceException("fail to move the folder",dae);
		}

		return folderkey;
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean moveFolder(ServiceContext<?> svcctx, InfoId<Long> folderkey, InfoId<Long> destinationPkey)
			throws ServiceException {

		try{
			
			InfoId<Long> fid = IdKey.CAB_FILE.getInfoId(folderkey.getId());
			return pseudodao.update(fid, FlatColumns.COL_FOLDER_PID, destinationPkey.getId()) > 0;
			
		}catch(DataAccessException dae){
			throw new ServiceException("fail to move the folder",dae);
		}
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public void addAce(ServiceContext<?> svcctx, InfoId<Long> folderkey, Ace ... aces)
			throws ServiceException {
		
		CabFolderInfo folderinfo = null;
		
		try{
			folderinfo = cabfolderdao.query(folderkey);
			Long aclid = folderinfo.getAclId();
			
			for(Ace ace : aces){
				
				CabAceInfo aceinfo = cabacedao.queryBySubject(aclid, ace.getType().value, ace.getSubject());
				
				if(aceinfo != null){
					// already have ace in database
					aceinfo.setSubject(ace.getSubject());
					aceinfo.setPrivilege(ace.getPrivilege());
					aceinfo.setPermissions(Cabinets.toPermString(ace.getPermissions()));
					svcctx.setTraceInfo(aceinfo);
					cabacedao.update(aceinfo);
				}else{
					// no ace then create new one.
					aceinfo = new CabAceInfo();
					InfoId<Long> infoId = idservice.generateId(IdKey.CAB_ACE, Long.class);
					aceinfo.setAclId(aclid);
					aceinfo.setInfoId(infoId);
					
					aceinfo.setSubject(ace.getSubject());
					aceinfo.setPrivilege(ace.getPrivilege());
					aceinfo.setPermissions(Cabinets.toPermString(ace.getPermissions()));
					svcctx.setTraceInfo(aceinfo);
					cabacedao.create(aceinfo);
				}
			}
		}catch(DataAccessException dae){
			
			throw new ServiceException("fail to set the ace list",dae);
		}
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public void addAcl(ServiceContext<?> svcctx, InfoId<Long> folderid, Acl acl)
			throws ServiceException {

		try{
			CabAclInfo aclinfo = new CabAclInfo();
			aclinfo.setInfoId(acl.getAclId());
			svcctx.setTraceInfo(aclinfo);
			
			if(cabacldao.create(aclinfo) > 0){
				for(Ace ace : acl.getAllAces()){
					
					CabAceInfo aceinfo = new CabAceInfo();
					aceinfo.setInfoId(ace.getAceId());
					aceinfo.setAclId(acl.getAclId().getId());
					aceinfo.setSubjectType(ace.getType().value);
					aceinfo.setSubject(ace.getSubject());
					aceinfo.setPrivilege(ace.getPrivilege());
					aceinfo.setPermissions(Cabinets.toPermString(ace.getPermissions()));
					svcctx.setTraceInfo(aceinfo);
					cabacedao.create(aceinfo);
					
				}
			}
			// update the cabinet file entry's acl_id
			InfoId<Long> fid = IdKey.CAB_FOLDER.getInfoId(folderid.getId());
			pseudodao.update(fid, FlatColumns.COL_ACL_ID, acl.getAclId().getId());
		}catch(DataAccessException dae){
			
			throw new ServiceException("fail to set the ace list",dae);
		}
	}

	@Transactional(value=ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public CabinetInfo getCabinetInfo(ServiceContext<?> svcctx, InfoId<Long> folderid)throws ServiceException{
	
		CabinetInfo rtv = null;
		StringBuffer qbuf = new StringBuffer("Select a.* from gp_cabinets a, gp_cab_folders b ");
		qbuf.append("Where b.cabinet_id = a.cabinet_id and b.folder_id = ?");
		
		Object[] params = new Object[]{
				folderid
		};
		
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : {} / params : {}", qbuf.toString(), ArrayUtils.toString(params));
		}
		try{
			rtv = jtemplate.queryForObject(qbuf.toString(), params, cabinetdao.getRowMapper());
		}catch(DataAccessException dae){
			throw new ServiceException("Fail get cabinet info", dae);
		}
		return rtv;
	}

	@Transactional(value=ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public CabFolderInfo getFolder(ServiceContext<?> svcctx, InfoId<Long> folderid) throws ServiceException {
		
		try{
			return cabfolderdao.query(folderid);
		}catch(DataAccessException dae){
			throw new ServiceException("Fail get folder info", dae);
		}

	}

	@Transactional(value=ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public InfoId<Long> getFolderId(ServiceContext<?> svcctx, InfoId<Long> cabinetId, String path)
			throws ServiceException {
	    
		SqlParameterSource in = new MapSqlParameterSource().
                  addValue("p_folder_path", path)
                  .addValue("p_cabinet_id", cabinetId.getId());
		
	    SimpleJdbcCall jdbcCall = pseudodao.getJdbcCall("proc_path2fid");
		Map<String, Object> out = jdbcCall.execute(in);
		
		Long id = Long.valueOf((Integer)out.get("p_folder_id"));
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("call procedure: proc_path2fid / params : cabid-{} ; path-{}",cabinetId, path);
		}
		return IdKey.CAB_FOLDER.getInfoId(id);
	}

	@Transactional(value=ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public String getFolderPath(ServiceContext<?> svcctx, InfoId<Long> folderId) throws ServiceException {
		
		SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_folder_id", folderId.getId());
		
	    SimpleJdbcCall jdbcCall = pseudodao.getJdbcCall("proc_fid2path");
		Map<String, Object> out = jdbcCall.execute(in);
		
		String path = (String) out.get("p_folder_path");
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("call procedure: proc_fid2path / params : foderid-{}", folderId.getId());
		}
		return path;
	}
}
