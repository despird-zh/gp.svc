package com.gp.svc.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gp.acl.Ace;
import com.gp.acl.Acl;
import com.gp.common.GeneralConstants;
import com.gp.common.IdKey;
import com.gp.common.ServiceContext;
import com.gp.config.ServiceConfigurator;
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
	// mapper to parse the json into Set<String> of permissions
	private static ObjectMapper mapper = new ObjectMapper();
		
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

	@Transactional(ServiceConfigurator.TRNS_MGR)
	@Override
	public InfoId<Long> newFolder(ServiceContext<?> svcctx, InfoId<Long> parentkey, CabFolderInfo file)
			throws ServiceException {
		
		if(InfoId.isValid(parentkey))
			file.setParentId(parentkey.getId());
		else
			file.setParentId(GeneralConstants.FOLDER_ROOT);
		
		InfoId<Long> fkey = null;
		// info key not set yet, create a new one and set it 
		if(!InfoId.isValid(file.getInfoId())){
			
			fkey = idservice.generateId(IdKey.CAB_FOLDER, Long.class);
			file.setInfoId(fkey);
		}
		try{
			svcctx.setTraceInfo(file);
			// create folder record
			cabfolderdao.create(file);
		}catch(DataAccessException dae){
			
			throw new ServiceException(dae);
		}
		return fkey;
	}

	@Transactional(ServiceConfigurator.TRNS_MGR)
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

	@Transactional(ServiceConfigurator.TRNS_MGR)
	@Override
	public void moveFolder(ServiceContext<?> svcctx, InfoId<Long> folderkey, InfoId<Long> destinationPkey)
			throws ServiceException {
	
		CabFolderInfo cfi = null;
		
		try{
			
			cfi = cabfolderdao.query(folderkey);
			cfi.setParentId(destinationPkey.getId());

			cabfolderdao.update(cfi);
		}catch(DataAccessException dae){
			throw new ServiceException("fail to move the folder",dae);
		}
	}

	@Transactional(ServiceConfigurator.TRNS_MGR)
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
					aceinfo.setPermissions(toPermissionString(ace.getPermissions()));
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
					aceinfo.setPermissions(toPermissionString(ace.getPermissions()));
					svcctx.setTraceInfo(aceinfo);
					cabacedao.create(aceinfo);
				}
			}
		}catch(DataAccessException dae){
			
			throw new ServiceException("fail to set the ace list",dae);
		}
	}

	@Transactional(ServiceConfigurator.TRNS_MGR)
	@Override
	public void addAcl(ServiceContext<?> svcctx, InfoId<Long> folderkey, Acl acl)
			throws ServiceException {
		
		CabFolderInfo folderinfo = null;
		
		try{
			// create new acl information
			InfoId<Long> aclId = idservice.generateId(IdKey.CAB_ACL, Long.class);
			CabAclInfo aclinfo = new CabAclInfo();
			aclinfo.setInfoId(aclId);
			svcctx.setTraceInfo(aclinfo);
			cabacldao.create(aclinfo);
			
			// update acl information to cab folder
			folderinfo = cabfolderdao.query(folderkey);
			folderinfo.setAclId(aclId.getId());
			cabfolderdao.update(folderinfo);
			
			for(Ace ace : acl.getAllAces()){
				CabAceInfo aceinfo = cabacedao.queryBySubject(aclId.getId(), ace.getType().value, ace.getSubject());
				
				if(aceinfo != null){
					// already have ace in database
					aceinfo.setSubject(ace.getSubject());
					aceinfo.setPrivilege(ace.getPrivilege());
					aceinfo.setPermissions(toPermissionString(ace.getPermissions()));
					svcctx.setTraceInfo(aceinfo);
					cabacedao.update( aceinfo);
				}else{
					// no ace then create new one.
					aceinfo = new CabAceInfo();
					InfoId<Long> infoId = idservice.generateId(IdKey.CAB_ACE, Long.class);
					aceinfo.setAclId(aclId.getId());
					aceinfo.setInfoId(infoId);
					
					aceinfo.setSubject(ace.getSubject());
					aceinfo.setPrivilege(ace.getPrivilege());
					aceinfo.setPermissions(toPermissionString(ace.getPermissions()));
					svcctx.setTraceInfo(aceinfo);
					cabacedao.create(aceinfo);
				}
			}
		}catch(DataAccessException dae){
			
			throw new ServiceException("fail to set the ace list",dae);
		}
	}

	public String toPermissionString(Set<String> permissions){
		
		String permstr;
		try {
			permstr = mapper.writeValueAsString(permissions);
		} catch (JsonProcessingException e) {
			LOGGER.error("fail to convert into string",e);
			permstr = "[]";
		}
		
		return permstr;
	}

	@Transactional(value=ServiceConfigurator.TRNS_MGR, readOnly=true)
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

	@Transactional(value=ServiceConfigurator.TRNS_MGR, readOnly=true)
	@Override
	public CabFolderInfo getFolder(ServiceContext<?> svcctx, InfoId<Long> folderid) throws ServiceException {
		
		try{
			return cabfolderdao.query(folderid);
		}catch(DataAccessException dae){
			throw new ServiceException("Fail get folder info", dae);
		}

	}
}
