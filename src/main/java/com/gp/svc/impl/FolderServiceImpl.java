package com.gp.svc.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gp.util.CommonUtils;
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
import com.gp.common.FlatColumns;
import com.gp.common.GeneralConstants;
import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.common.ServiceContext;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.CabAceDAO;
import com.gp.dao.CabAclDAO;
import com.gp.dao.CabFileDAO;
import com.gp.dao.CabFolderDAO;
import com.gp.dao.CabinetDAO;
import com.gp.dao.PseudoDAO;
import com.gp.exception.ServiceException;
import com.gp.dao.info.CabAceInfo;
import com.gp.dao.info.CabAclInfo;
import com.gp.dao.info.CabFileInfo;
import com.gp.dao.info.CabFolderInfo;
import com.gp.dao.info.CabinetInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.info.InfoIdHelper;
import com.gp.svc.FolderService;
import com.gp.util.DateTimeUtils;
import com.gp.svc.CommonService;

@Service
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
	public InfoId<Long> newFolder(ServiceContext svcctx, InfoId<Long> parentkey, CabFolderInfo folder, Acl acl)
			throws ServiceException {
		
		if(InfoIdHelper.isValid(parentkey))
			folder.setParentId(parentkey.getId());
		else
			folder.setParentId(GeneralConstants.FOLDER_ROOT);
		
		InfoId<Long> fkey = null;
		// info key not set yet, create a new one and set it 
		if(!InfoIdHelper.isValid(folder.getInfoId())){
			
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
			
			throw new ServiceException("excp.create", dae, "Cabinet folder");
		}
		return fkey;
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public InfoId<Long> copyFolder(ServiceContext svcctx, InfoId<Long> folderid, InfoId<Long> destFolderId)
			throws ServiceException {
		
		CabFolderInfo cfi = null;
		
		try{
			cfi = cabfolderdao.query(folderid);
			// new folder key
			InfoId<Long> fkey = idservice.generateId(IdKey.CAB_FOLDER, Long.class);
			cfi.setInfoId(fkey);
			cfi.setParentId(destFolderId.getId());
			svcctx.setTraceInfo(cfi);
			// move the current folder 
			cabfolderdao.create(cfi);
			
			// find child sub folders
			List<CabFolderInfo> flist = cabfolderdao.queryByParent(folderid.getId());
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
				svcctx.setTraceInfo(fileinfo);
				cabfiledao.create(fileinfo);
			}
			
		}catch(DataAccessException dae){
			throw new ServiceException("excp.copy.folder", dae, folderid, destFolderId);
		}

		return folderid;
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean moveFolder(ServiceContext svcctx, InfoId<Long> folderkey, InfoId<Long> destFolderId)
			throws ServiceException {

		try{
			// recreate to ensure the id column name is correct.
			InfoId<Long> fid = IdKeys.getInfoId(IdKey.CAB_FOLDER, folderkey.getId());
			
			Map<FlatColLocator, Object> colmap = new HashMap<FlatColLocator, Object>();
			colmap.put(FlatColumns.FOLDER_PID, destFolderId.getId());
			colmap.put(FlatColumns.MODIFIER, svcctx.getPrincipal().getAccount());
			colmap.put(FlatColumns.MODIFY_DATE, DateTimeUtils.now());
			
			return pseudodao.update(fid, colmap) > 0;
			
		}catch(DataAccessException dae){
			throw new ServiceException("excp.move.folder", dae, folderkey, destFolderId);
		}
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public void addAce(ServiceContext svcctx, InfoId<Long> folderkey, Ace ... aces)
			throws ServiceException {
		
		try{
			Long aclid = pseudodao.query(folderkey, FlatColumns.ACL_ID, Long.class);	
	
			for(Ace ace : aces){
				
				CabAceInfo aceinfo = cabacedao.queryBySubject(aclid, ace.getType().value, ace.getSubject());
				
				if(aceinfo != null){
					// already have ace in database
					aceinfo.setSubject(ace.getSubject());
					aceinfo.setPrivileges(CommonUtils.toJson(ace.getPrivileges()));
					aceinfo.setPermissions(CommonUtils.toJson(ace.getPermissions()));
					svcctx.setTraceInfo(aceinfo);
					cabacedao.update(aceinfo,FilterMode.NONE);
				}else{
					// no ace then create new one.
					aceinfo = new CabAceInfo();
					InfoId<Long> infoId = idservice.generateId(IdKey.CAB_ACE, Long.class);
					aceinfo.setAclId(aclid);
					aceinfo.setInfoId(infoId);
					
					aceinfo.setSubject(ace.getSubject());
					aceinfo.setPrivileges(CommonUtils.toJson(ace.getPrivileges()));
					aceinfo.setPermissions(CommonUtils.toJson(ace.getPermissions()));
					svcctx.setTraceInfo(aceinfo);
					cabacedao.create(aceinfo);
				}
			}
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.add.ace.to", dae, folderkey);
		}
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public void addAcl(ServiceContext svcctx, InfoId<Long> folderid, Acl acl)
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
					aceinfo.setPrivileges(CommonUtils.toJson(ace.getPrivileges()));
					aceinfo.setPermissions(CommonUtils.toJson(ace.getPermissions()));
					svcctx.setTraceInfo(aceinfo);
					cabacedao.create(aceinfo);
					
				}
			}
			// update the cabinet file entry's acl_id
			InfoId<Long> fid = IdKeys.getInfoId(IdKey.CAB_FOLDER, folderid.getId());
			pseudodao.update(fid, FlatColumns.ACL_ID, acl.getAclId().getId());
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.set.acl",dae, folderid);
		}
	}

	@Transactional(value=ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public CabinetInfo getCabinetInfo(ServiceContext svcctx, InfoId<Long> folderid)throws ServiceException{
	
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
			rtv = jtemplate.queryForObject(qbuf.toString(), params, CabinetDAO.CabinetMapper);
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with",dae, "cabinet", folderid);
		}
		return rtv;
	}

	@Transactional(value=ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public CabFolderInfo getFolder(ServiceContext svcctx, InfoId<Long> folderid) throws ServiceException {
		
		try{
			return cabfolderdao.query(folderid);
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with",dae, "folder", folderid);
		}

	}

	@Transactional(value=ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public InfoId<Long> getFolderId(ServiceContext svcctx, InfoId<Long> cabinetId, String path)
			throws ServiceException {
	    
		SqlParameterSource in = new MapSqlParameterSource().
                  addValue("p_folder_path", path)
                  .addValue("p_cabinet_id", cabinetId.getId());
		
	    SimpleJdbcCall jdbcCall = pseudodao.getJdbcCall("proc_path2fid");
	    Long id = null;
	    try{
			Map<String, Object> out = jdbcCall.execute(in);
			id = Long.valueOf((Integer)out.get("p_folder_id"));
	    }catch(DataAccessException dae){
	    	throw new ServiceException("excp.proc.with",dae, "proc_path2fid", path);
	    }
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("call procedure: proc_path2fid / params : cabid-{} ; path-{}",cabinetId, path);
		}
		return IdKeys.getInfoId(IdKey.CAB_FOLDER, id);
	}

	@Transactional(value=ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public String getFolderPath(ServiceContext svcctx, InfoId<Long> folderId) throws ServiceException {
		
		SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_folder_id", folderId.getId());
		
	    SimpleJdbcCall jdbcCall = pseudodao.getJdbcCall("proc_fid2path");
	    String path = null;
	    try{
	    	Map<String, Object> out = jdbcCall.execute(in);
	    	path = (String) out.get("p_folder_path");
	    }catch(DataAccessException dae){
	    	throw new ServiceException("excp.proc.with",dae, "proc_fid2path", folderId);
	    }
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("call procedure: proc_fid2path / params : foderid-{}", folderId.getId());
		}
		return path;
	}
}
