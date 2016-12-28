package com.gp.svc.impl;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gp.acl.Ace;
import com.gp.acl.AceType;
import com.gp.acl.Acl;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.common.IdKey;
import com.gp.common.ServiceContext;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.CabAceDAO;
import com.gp.dao.CabAclDAO;
import com.gp.dao.PseudoDAO;
import com.gp.exception.ServiceException;
import com.gp.dao.info.CabAceInfo;
import com.gp.dao.info.CabAclInfo;
import com.gp.info.InfoId;
import com.gp.svc.AclService;
import com.gp.svc.CommonService;

@Service
public class AclServiceImpl implements AclService{
	
	Logger LOGGER = LoggerFactory.getLogger(AclServiceImpl.class);
	// mapper to parse the json into Set<String> of permissions
	private static ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	PseudoDAO pseudodao;
	
	@Autowired
	CabAceDAO cabacedao;
	
	@Autowired
	CabAclDAO cabacldao;
	
	@Autowired
	CommonService idService;
	
	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public InfoId<Long> addAclInfo(ServiceContext svcctx, List<CabAceInfo> aces)  throws ServiceException{

		try{
			InfoId<Long> aclid = idService.generateId(IdKey.CAB_ACL, Long.class);
			CabAclInfo cabaclinfo = new CabAclInfo();
			cabaclinfo.setInfoId(aclid);
			svcctx.setTraceInfo(cabaclinfo);
			cabacldao.create(cabaclinfo);
			
			for(CabAceInfo ace : aces){
				InfoId<Long> aceid = idService.generateId(IdKey.CAB_ACE, Long.class);
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

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public InfoId<Long> addAceInfo(ServiceContext svcctx, InfoId<Long> aclId, CabAceInfo ace) throws ServiceException{
		
		try{
			CabAceInfo aceinfo = cabacedao.queryBySubject(aclId.getId(), 
					ace.getSubjectType(), 
					ace.getSubject());
			
			if(aceinfo != null){
				
				aceinfo.setPrivilege(ace.getPrivilege());
				aceinfo.setPermissions(ace.getPermissions());
				cabacedao.update( aceinfo, FilterMode.NONE);
				
				return aceinfo.getInfoId();
			}else{
				
				InfoId<Long> aceid = idService.generateId(IdKey.CAB_ACE, Long.class);
				ace.setInfoId(aceid);
				ace.setAclId(aclId.getId());
				cabacedao.create(ace);
				
				return ace.getInfoId();
			}
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.add.ace", dae, aclId);
		}
	}
	
	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public void addAceInfos(ServiceContext svcctx, InfoId<Long> aclId, CabAceInfo ... aces)  throws ServiceException{
		try{
			for(CabAceInfo ace : aces){
				
				CabAceInfo aceinfo = cabacedao.queryBySubject(aclId.getId(), 
						ace.getSubjectType(), 
						ace.getSubject());
				
				if(aceinfo != null){
					aceinfo.setPrivilege(ace.getPrivilege());
					aceinfo.setPermissions(ace.getPermissions());
					cabacedao.update( aceinfo, FilterMode.NONE);
				}else{
					InfoId<Long> aceid = idService.generateId(IdKey.CAB_ACE, Long.class);
					ace.setInfoId(aceid);
					ace.setAclId(aclId.getId());
					cabacedao.create(ace);
				}
			}
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.add.ace", dae, aclId);
		}

	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
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

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
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
				ace.setPrivilege(cai.getPrivilege(), true);
				Set<String> perms = null;
				// parse the permission set
				try {
					perms = mapper.readValue(cai.getPermissions(), new TypeReference<Set<String>>(){});
				} catch (Exception e) {
					
					throw new ServiceException("Fail to parse the permissions string", e);
				} 
	
				ace.setPermissions(perms, true);
				// add to acl.
				acl.addAce(ace, false);
			}
			return acl;
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with", dae, "ACL", aclId);
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public Ace getAce(ServiceContext svcctx, InfoId<Long> aclId, String aceType, String subject)  throws ServiceException{
		
		try{
			CabAceInfo aci = cabacedao.queryBySubject(aclId.getId(), aceType, subject);
			
			if(null == aci)
				return null;
			
			AceType type = AceType.parse(aci.getSubjectType());
			Ace ace = new Ace(type, aci.getSubject());
			ace.setAceId(aci.getInfoId());
			ace.setPrivilege(aci.getPrivilege(), true);
			Set<String> perms = null;
			// parse the permission set
			try {
				perms = mapper.readValue(aci.getPermissions(), new TypeReference<Set<String>>(){});
			} catch (Exception e) {
				
				throw new ServiceException("Fail to parse the permissions string", e);
			} 
	
			ace.setPermissions(perms, true);
			return ace;
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with", dae, "ACE", aclId);
		}
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
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

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean removeAce(ServiceContext svcctx, InfoId<Long> aclId, String aceType, String subject)  throws ServiceException{
		
		try{
			return cabacedao.deleteBySubject(aclId.getId(), aceType, subject) > 0;
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.delete.with", dae, "ACE", "aclid="+aclId.getId() + "/type="+aceType + "/subject"+subject);
		}
	}

}
