package com.gp.svc.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gp.acl.Ace;
import com.gp.acl.AcePrivilege;
import com.gp.acl.AceType;
import com.gp.acl.Acl;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.common.IdKey;
import com.gp.common.ServiceContext;
import com.gp.common.DataSourceHolder;
import com.gp.dao.CabAceDAO;
import com.gp.dao.CabAclDAO;
import com.gp.dao.PseudoDAO;
import com.gp.exception.ServiceException;
import com.gp.dao.info.CabAceInfo;
import com.gp.dao.info.CabAclInfo;
import com.gp.info.InfoId;
import com.gp.svc.AclService;
import com.gp.svc.CommonService;
import com.gp.util.CommonUtils;

@Service
public class AclServiceImpl implements AclService{
	
	Logger LOGGER = LoggerFactory.getLogger(AclServiceImpl.class);
	
	@Autowired
	PseudoDAO pseudodao;
	
	@Autowired
	CabAceDAO cabacedao;
	
	@Autowired
	CabAclDAO cabacldao;
	
	@Autowired
	CommonService idService;
	
	@Transactional(DataSourceHolder.TRNS_MGR)
	@Override
	public InfoId<Long> addAclInfo(ServiceContext svcctx, List<CabAceInfo> aces)  throws ServiceException{

		try{
			InfoId<Long> aclid = idService.generateId(IdKey.GP_CAB_ACL, Long.class);
			CabAclInfo cabaclinfo = new CabAclInfo();
			cabaclinfo.setInfoId(aclid);
			svcctx.setTraceInfo(cabaclinfo);
			cabacldao.create(cabaclinfo);
			
			for(CabAceInfo ace : aces){
				InfoId<Long> aceid = idService.generateId(IdKey.GP_CAB_ACE, Long.class);
				ace.setInfoId(aceid);
				ace.setAclId(aclid.getId());
				svcctx.setTraceInfo(ace);
				cabacedao.create(ace);
			}
			
			return aclid;
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.create.with", dae, "ACL","ACES");
		}
	}

	@Transactional(DataSourceHolder.TRNS_MGR)
	@Override
	public InfoId<Long> addAceInfo(ServiceContext svcctx, InfoId<Long> aclId, CabAceInfo ace) throws ServiceException{
		
		try{
			CabAceInfo aceinfo = cabacedao.queryBySubject(aclId.getId(), 
					ace.getSubjectType(), 
					ace.getSubject());
			
			if(aceinfo != null){
				
				aceinfo.setPrivileges(ace.getPrivileges());
				aceinfo.setPermissions(ace.getPermissions());
				cabacedao.update( aceinfo, FilterMode.NONE);
				
				return aceinfo.getInfoId();
			}else{
				
				InfoId<Long> aceid = idService.generateId(IdKey.GP_CAB_ACE, Long.class);
				ace.setInfoId(aceid);
				ace.setAclId(aclId.getId());
				cabacedao.create(ace);
				
				return ace.getInfoId();
			}
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.add.ace", dae, aclId);
		}
	}
	
	@Transactional(DataSourceHolder.TRNS_MGR)
	@Override
	public void addAceInfos(ServiceContext svcctx, InfoId<Long> aclId, CabAceInfo ... aces)  throws ServiceException{
		try{
			for(CabAceInfo ace : aces){
				
				CabAceInfo aceinfo = cabacedao.queryBySubject(aclId.getId(), 
						ace.getSubjectType(), 
						ace.getSubject());
				
				if(aceinfo != null){
					aceinfo.setPrivileges(ace.getPrivileges());
					aceinfo.setPermissions(ace.getPermissions());
					cabacedao.update( aceinfo, FilterMode.NONE);
				}else{
					InfoId<Long> aceid = idService.generateId(IdKey.GP_CAB_ACE, Long.class);
					ace.setInfoId(aceid);
					ace.setAclId(aclId.getId());
					cabacedao.create(ace);
				}
			}
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.add.ace", dae, aclId);
		}

	}

	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public CabAceInfo getAceInfo(ServiceContext svcctx, InfoId<Long> aclId, String aceType, String subject)  throws ServiceException{
		
		try{
			CabAceInfo aceinfo = cabacedao.queryBySubject(aclId.getId(), 
					aceType, 
					subject);
			
			return aceinfo;
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with", dae, "ACE", aclId);
		}
	}

	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public Acl getAcl(ServiceContext svcctx, InfoId<Long> aclId)  throws ServiceException{
		
		try{
			CabAclInfo aclinfo = cabacldao.query(aclId);
			Acl acl = new Acl();
			acl.setAclId(aclinfo.getInfoId());
			// find ace list of acl
			List<CabAceInfo> acelist = cabacedao.queryByAcl(aclId.getId());
			for(CabAceInfo cai : acelist){
				// parse type
				AceType type = AceType.parse(cai.getSubjectType());
				Ace ace = new Ace(type, cai.getSubject());
				ace.setAceId(cai.getInfoId());
				
				Set<String> perms = null;
				Set<String> privileges = null;
				try {
					perms = CommonUtils.JSON_MAPPER.readValue(cai.getPermissions(), new TypeReference<Set<String>>(){});
					ace.setPermissions(perms, true);
					privileges = CommonUtils.JSON_MAPPER.readValue(cai.getPrivileges(), new TypeReference<Set<String>>(){});
					for(String priv: privileges) {
						ace.grantPrivileges(AcePrivilege.parse(priv));
					}
					if(cai.getBrowsable()) {
						ace.grantPrivileges(AcePrivilege.BROWSE);
					}
				} catch (Exception e) {
					
					throw new ServiceException("Fail to parse the permissions string", e);
				} 

				// add to acl.
				acl.addAce(ace, false);
			}
			return acl;
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with", dae, "ACL", aclId);
		}
	}

	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public Ace getAce(ServiceContext svcctx, InfoId<Long> aclId, String aceType, String subject)  throws ServiceException{
		
		try{
			CabAceInfo aci = cabacedao.queryBySubject(aclId.getId(), aceType, subject);
			
			if(null == aci)
				return null;
			
			AceType type = AceType.parse(aci.getSubjectType());
			Ace ace = new Ace(type, aci.getSubject());
			ace.setAceId(aci.getInfoId());
			Set<String> perms = null;
			Set<String> privileges = null;
			// parse the permission set
			try {
				perms = CommonUtils.JSON_MAPPER.readValue(aci.getPermissions(), new TypeReference<Set<String>>(){});
				ace.setPermissions(perms, true);
				privileges = CommonUtils.JSON_MAPPER.readValue(aci.getPrivileges(), new TypeReference<Set<String>>(){});
				for(String priv: privileges) {
					ace.grantPrivileges(AcePrivilege.parse(priv));
				}
				if(aci.getBrowsable()) {
					ace.grantPrivileges(AcePrivilege.BROWSE);
				}
			} catch (Exception e) {
				
				throw new ServiceException("Fail to parse the permissions string", e);
			} 

			return ace;
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with", dae, "ACE", aclId);
		}
	}

	@Transactional(DataSourceHolder.TRNS_MGR)
	@Override
	public boolean removeAcl(ServiceContext svcctx, InfoId<Long> aclId)  throws ServiceException{

		try{
			// delete the related aces
			cabacedao.deleteByAcl(aclId.getId());
			return cabacldao.delete(aclId) > 0;
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.delete.with", dae, "ACL", aclId);
		}
	}

	@Transactional(DataSourceHolder.TRNS_MGR)
	@Override
	public boolean removeAce(ServiceContext svcctx, InfoId<Long> aclId, String aceType, String subject)  throws ServiceException{
		
		try{
			return cabacedao.deleteBySubject(aclId.getId(), aceType, subject) > 0;
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.delete.with", dae, "ACE", "aclid="+aclId.getId() + "/type="+aceType + "/subject"+subject);
		}
	}

	@Override
	public InfoId<Long> addAcl(ServiceContext svcctx, Acl acl) throws ServiceException {
		try{
			
			InfoId<Long> aclid = idService.generateId(IdKey.GP_CAB_ACL, Long.class);
			CabAclInfo cabaclinfo = new CabAclInfo();
			cabaclinfo.setInfoId(aclid);
			svcctx.setTraceInfo(cabaclinfo);
			cabacldao.create(cabaclinfo);
			
			CabAceInfo aceinfo = null;
			for(Ace ace : acl.getAllAces()){
				aceinfo = new CabAceInfo();
				InfoId<Long> aceid = idService.generateId(IdKey.GP_CAB_ACE, Long.class);
				aceinfo.setInfoId(aceid);
				aceinfo.setAclId(aclid.getId());
				aceinfo.setSubjectType(ace.getType().value);
				aceinfo.setSubject(ace.getSubject());
				aceinfo.setBrowsable(ace.checkPrivilege(AcePrivilege.BROWSE));
				Set<AcePrivilege> pset = ace.getPrivileges();
				pset.remove(AcePrivilege.BROWSE);
				Set<String> privs = new HashSet<String>();
				for(AcePrivilege priv: pset) {
					privs.add(priv.value);
				}
				aceinfo.setPrivileges(CommonUtils.toJson(privs));
				aceinfo.setPermissions(CommonUtils.toJson(ace.getPermissions()));
				
				svcctx.setTraceInfo(aceinfo);
				cabacedao.create(aceinfo);
			}
			
			return aclid;
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.create.with", dae, "ACL","ACES");
		}
	}

	@Override
	public InfoId<Long> addAce(ServiceContext svcctx,  InfoId<Long> aclId, Ace ace) throws ServiceException {
		try{
			
			CabAceInfo aceinfo = cabacedao.queryBySubject(ace.getAceId().getId(), 
					ace.getType().toString(), 
					ace.getSubject());
			
			if(aceinfo != null){
				aceinfo.setBrowsable(ace.checkPrivilege(AcePrivilege.BROWSE));
				aceinfo.setPrivileges(CommonUtils.toJson(ace.getPrivileges()));
				aceinfo.setPermissions(CommonUtils.toJson(ace.getPermissions()));
				
				cabacedao.update( aceinfo, FilterMode.NONE);
				
				return aceinfo.getInfoId();
			}else{
				
				aceinfo = new CabAceInfo();
				InfoId<Long> aceid = idService.generateId(IdKey.GP_CAB_ACE, Long.class);
				
				aceinfo.setInfoId(aceid);
				aceinfo.setAclId(aclId.getId());
				aceinfo.setSubject(ace.getSubject());
				aceinfo.setBrowsable(ace.checkPrivilege(AcePrivilege.BROWSE));
				aceinfo.setPrivileges(CommonUtils.toJson(ace.getPrivileges()));
				aceinfo.setPermissions(CommonUtils.toJson(ace.getPermissions()));
				
				svcctx.setTraceInfo(aceinfo);
				cabacedao.create(aceinfo);
				
				return aceid;
			}
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.add.ace", dae, aclId);
		}
	}

}
