package com.gp.svc.impl;

import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.acl.Ace;
import com.gp.acl.Acl;
import com.gp.common.Cabinets;
import com.gp.common.IdKey;
import com.gp.common.ServiceContext;
import com.gp.config.ServiceConfigurator;
import com.gp.dao.CabAceDAO;
import com.gp.dao.CabAclDAO;
import com.gp.dao.CabFileDAO;
import com.gp.dao.CabVersionDAO;
import com.gp.dao.CabinetDAO;
import com.gp.dao.PseudoDAO;
import com.gp.dao.StorageDAO;
import com.gp.exception.ServiceException;
import com.gp.info.CabAceInfo;
import com.gp.info.CabAclInfo;
import com.gp.info.CabFileInfo;
import com.gp.info.CabVersionInfo;
import com.gp.info.CabinetInfo;
import com.gp.info.InfoId;
import com.gp.info.StorageInfo;
import com.gp.svc.FileService;
import com.gp.svc.CommonService;
import com.gp.util.DateTimeUtils;

@Service("fileService")
public class FileServiceImpl implements FileService{

	static Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);
	
	@Autowired
	CabVersionDAO cabversiondao;
	
	@Autowired
	CabFileDAO cabfiledao;
	
	@Autowired
	StorageDAO storagedao;
	
	@Autowired
	PseudoDAO pseudodao;
	
	@Autowired 
	CabAclDAO acldao;
	
	@Autowired 
	CabAceDAO acedao;
	
	@Autowired
	CabinetDAO cabinetdao;
	
	@Autowired
	private CommonService idservice;
	
	@Transactional(value=ServiceConfigurator.TRNS_MGR, readOnly=true)
	@Override
	public List<CabVersionInfo> getVersions(ServiceContext<?> svcctx, InfoId<Long> filekey) throws ServiceException {
		
		List<CabVersionInfo> versions = null;
		
		try{
			
			versions = cabversiondao.queryByFileId(filekey.getId());
		}catch(DataAccessException dae){
			
			throw new ServiceException("fail to query", dae);
		}
		return versions;
	}

	@Transactional(ServiceConfigurator.TRNS_MGR)
	@Override
	public InfoId<Long> newFile(ServiceContext<?> svcctx, CabFileInfo file, Acl acl) throws ServiceException {

		InfoId<Long> fkey = file.getInfoId();
		try{
			
			if(!InfoId.isValid(fkey)){
				fkey = idservice.generateId(IdKey.CAB_FILE, Long.class);
				file.setInfoId(fkey);
			}
			
			// create file entry
			svcctx.setTraceInfo(file);
			cabfiledao.create(file);
			
			addAcl(svcctx, file.getInfoId(), acl);
		}catch(DataAccessException dae){
			
			throw new ServiceException("fail to query", dae);
		}
		return fkey;
	}

	@Transactional(ServiceConfigurator.TRNS_MGR)
	@Override
	public InfoId<Long> copyFile(ServiceContext<?> svcctx, InfoId<Long> srcfilekey, InfoId<Long> destinationPkey)
			throws ServiceException {
		
		CabFileInfo cfileinfo = null;
		InfoId<Long> fkey = null;
		try{
			
			cfileinfo = cabfiledao.query(srcfilekey);
			fkey = idservice.generateId(IdKey.CAB_FILE, Long.class);
			cfileinfo.setInfoId(fkey);
			
			cabfiledao.create(cfileinfo);
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("fail to query", dae);
		}
		return fkey;
	}

	@Transactional(ServiceConfigurator.TRNS_MGR)
	@Override
	public void moveFile(ServiceContext<?> svcctx, InfoId<Long> srcfilekey, InfoId<Long> destinationPkey)
			throws ServiceException {
		
		CabFileInfo cfileinfo = null;
		try{
			
			cfileinfo = cabfiledao.query(srcfilekey);
			
			cfileinfo.setParentId(destinationPkey.getId());
			
			cabfiledao.update(cfileinfo);
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("fail to query", dae);
		}

	}

	@Transactional(ServiceConfigurator.TRNS_MGR)
	@Override
	public InfoId<Long> newVersion(ServiceContext<?> svcctx, InfoId<Long> filekey) throws ServiceException {

		CabFileInfo cfileinfo = null;
		InfoId<Long> vkey = null;
		try{
			
			cfileinfo = cabfiledao.query(filekey);
			CabVersionInfo versioninfo = new CabVersionInfo();
			
			vkey = idservice.generateId(IdKey.CAB_VERSION, Long.class);
			
			versioninfo.setBinaryId(cfileinfo.getBinaryId());
			versioninfo.setCabinetId(cfileinfo.getCabinetId());
			versioninfo.setCommentOn(cfileinfo.isCommentOn());
			versioninfo.setCreateDate(DateTimeUtils.now());
			versioninfo.setCreator(svcctx.getPrincipal().getAccount());
			versioninfo.setDescription(cfileinfo.getDescription());
			versioninfo.setFileId(cfileinfo.getInfoId().getId());
			versioninfo.setFileName(cfileinfo.getEntryName());
			versioninfo.setFormat(cfileinfo.getFormat());

			versioninfo.setOwm(cfileinfo.getOwm());
			versioninfo.setOwner(cfileinfo.getOwner());
			versioninfo.setParentId(cfileinfo.getParentId());
			versioninfo.setProfile(cfileinfo.getProfile());
			versioninfo.setProperties(cfileinfo.getProperties());
			versioninfo.setSize(cfileinfo.getSize());
			versioninfo.setState(cfileinfo.getState());
			versioninfo.setVersion(cfileinfo.getVersion());
			versioninfo.setVersionLabel(cfileinfo.getVersionLabel());
			
			svcctx.setTraceInfo(versioninfo);
			cabversiondao.create(versioninfo);
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("fail to query", dae);
		}
		return vkey;
	}

	@Transactional(ServiceConfigurator.TRNS_MGR)
	@Override
	public void addAce(ServiceContext<?> svcctx, InfoId<Long> cabfileId, Ace ace) throws ServiceException {
		
		CabFileInfo fileinfo = cabfiledao.query(cabfileId);
		Long aclid = fileinfo.getAclId();
		// find available ace entry 
		CabAceInfo aceinfo = acedao.queryBySubject(aclid, ace.getType().value, ace.getSubject());
		if(aceinfo == null){
			// ace not exist yet
			aceinfo = new CabAceInfo();
			aceinfo.setInfoId(ace.getAceId());
			aceinfo.setAclId(aclid);
			aceinfo.setSubjectType(ace.getType().value);
			aceinfo.setSubject(ace.getSubject());
			aceinfo.setPermissions(Cabinets.toPermString(ace.getPermissions()));
			svcctx.setTraceInfo(aceinfo);
			acedao.create(aceinfo);
			
		}else{
			
			aceinfo.setSubjectType(ace.getType().value);
			aceinfo.setSubject(ace.getSubject());
			aceinfo.setPermissions(Cabinets.toPermString(ace.getPermissions()));
			svcctx.setTraceInfo(aceinfo);
			
			acedao.update(aceinfo);
		}
	}

	@Transactional(ServiceConfigurator.TRNS_MGR)
	@Override
	public void removeAce(ServiceContext<?> svcctx, InfoId<Long> cabfileId, String type,String subject) throws ServiceException {
		
		CabFileInfo fileinfo = cabfiledao.query(cabfileId);
		Long aclid = fileinfo.getAclId();
		acedao.deleteBySubject(aclid, type, subject);
	}
	
	@Transactional(ServiceConfigurator.TRNS_MGR)
	@Override
	public void addAcl(ServiceContext<?> svcctx, InfoId<Long> cabfileId, Acl acl) throws ServiceException {
		
		CabAclInfo aclinfo = new CabAclInfo();
		aclinfo.setInfoId(acl.getAclId());
		svcctx.setTraceInfo(aclinfo);
		
		if(acldao.create(aclinfo) > 0){
			for(Ace ace : acl.getAllAces()){
				
				CabAceInfo aceinfo = new CabAceInfo();
				aceinfo.setInfoId(ace.getAceId());
				aceinfo.setAclId(acl.getAclId().getId());
				aceinfo.setSubjectType(ace.getType().value);
				aceinfo.setSubject(ace.getSubject());
				aceinfo.setPermissions(Cabinets.toPermString(ace.getPermissions()));
				svcctx.setTraceInfo(aceinfo);
				acedao.create(aceinfo);
				
			}
		}
		// update the cabinet file entry's acl_id
		InfoId<Long> fid = IdKey.CAB_FILE.getInfoId(cabfileId.getId());
		pseudodao.update(fid, Cabinets.COL_ACL_ID, acl.getAclId().getId());
	}

	@Transactional(value=ServiceConfigurator.TRNS_MGR, readOnly=true)
	@Override
	public StorageInfo getStorage(InfoId<Long> fileid) throws ServiceException {
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("SELECT a.*");
		SQL.append("FROM gp_storages a, gp_cabinets b, gp_cab_files c ");
		SQL.append("  WHERE a.storage_id = b.storage_id ");
		SQL.append("     AND b.cabinet_id = c.cabinet_id ");
		SQL.append("     AND c.file_id = ? ");
		
		Object[] params  = new Object[]{
				fileid.getId()
		};
		
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, ArrayUtils.toString(params));
		}
		try{
			List<StorageInfo> storages = jtemplate.query(SQL.toString(), params, storagedao.getRowMapper());
		
			return CollectionUtils.isEmpty(storages)? null : storages.get(0);
		}catch(DataAccessException dae){
			
			throw new ServiceException("Fail to query the storage of cabinet file.",dae);
		}
	}

	@Transactional(value=ServiceConfigurator.TRNS_MGR, readOnly=true)
	@Override
	public CabinetInfo getCabinetInfo(ServiceContext<?> svcctx, InfoId<Long> fileid) throws ServiceException {
		CabinetInfo rtv = null;
		StringBuffer qbuf = new StringBuffer("Select a.* from gp_cabinets a, gp_cab_files b ");
		qbuf.append("Where b.cabinet_id = a.cabinet_id and b.file_id = ?");
		
		Object[] params = new Object[]{
				fileid.getId()
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
	public CabFileInfo getFile(ServiceContext<?> svcctx, InfoId<Long> fileid) throws ServiceException {
		try{
			return cabfiledao.query(fileid);
		}catch(DataAccessException dae){
			throw new ServiceException("Fail get folder info", dae);
		}
	}


}
