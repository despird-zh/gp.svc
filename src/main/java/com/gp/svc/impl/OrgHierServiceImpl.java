package com.gp.svc.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.common.GeneralConstants;
import com.gp.common.IdKey;
import com.gp.common.ServiceContext;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.GroupDAO;
import com.gp.dao.GroupUserDAO;
import com.gp.dao.OrgHierDAO;
import com.gp.dao.OrgUserDAO;
import com.gp.dao.PseudoDAO;
import com.gp.dao.UserDAO;
import com.gp.exception.ServiceException;
import com.gp.info.GroupInfo;
import com.gp.info.GroupUserInfo;
import com.gp.info.InfoId;
import com.gp.info.OrgHierInfo;
import com.gp.info.OrgUserInfo;
import com.gp.info.UserInfo;
import com.gp.svc.CommonService;
import com.gp.svc.OrgHierService;

@Service("orghierService")
public class OrgHierServiceImpl implements OrgHierService{

	Logger LOGGER = LoggerFactory.getLogger(OrgHierServiceImpl.class);

	@Autowired
	private PseudoDAO pseudodao;
	
	@Autowired
	private OrgHierDAO orghierdao;

	@Autowired
	private OrgUserDAO orguserdao;
	
	@Autowired
	private GroupDAO groupdao;
	
	@Autowired
	private GroupUserDAO groupuserdao;

	@Autowired
	private UserDAO userdao;
	
	@Autowired
	private CommonService idservice;

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public List<OrgHierInfo> getOrgHierNodes(ServiceContext<?> svcctx, Long parentNodeId)
			throws ServiceException {

		List<OrgHierInfo> orglist = null;
		
		StringBuffer SQL = new StringBuffer("select * from gp_org_hier where org_pid = ?");
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + parentNodeId);
		}
		try{
			
			orglist = jtemplate.query(SQL.toString(), new Object[]{parentNodeId}, orghierdao.getRowMapper());
		
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with", dae, "Org hier", parentNodeId);
		}
		
		return orglist;
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean newOrgHierNode(ServiceContext<?> svcctx, OrgHierInfo orginfo) throws ServiceException {
		
		try{
			svcctx.setTraceInfo(orginfo);
			// prepare the group information
			GroupInfo ginfo = new GroupInfo();
			InfoId<Long> gid = idservice.generateId(IdKey.GROUP, Long.class);
			ginfo.setInfoId(gid);
			// org group with fixed workgroup -888
			ginfo.setWorkgroupId(GeneralConstants.ORGHIER_WORKGROUP);
			ginfo.setGroupName(orginfo.getOrgName() + "'s group");
			ginfo.setDescription(orginfo.getDescription());
			// set trace information
			svcctx.setTraceInfo(ginfo);
			// create group
			groupdao.create(ginfo);
			// update org hier info with group id
			orginfo.setGroupId(gid.getId());
			
			return orghierdao.create(orginfo) > 0;
		
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.create", dae, "Org hierarchy");
		}
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean saveOrgHierNode(ServiceContext<?> svcctx, OrgHierInfo orginfo) throws ServiceException {
		try{
			
			svcctx.setTraceInfo(orginfo);
			return orghierdao.update(orginfo) > 0;
		
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.update", dae, "org hierarchy");
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public OrgHierInfo getOrgHierNode(ServiceContext<?> svcctx, InfoId<Long> orgid) throws ServiceException {
		try{

			return orghierdao.query(orgid);
		
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with", dae, "org hierarchy", orgid);
		}
	}
	
	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean removeOrgHierNode(ServiceContext<?> svcctx, InfoId<Long> orgid) throws ServiceException {

		try{
			OrgHierInfo orginfo = orghierdao.query(orgid);
			InfoId<Long> org_grpid = IdKey.GROUP.getInfoId(orginfo.getGroupId());
			groupuserdao.deleteByGroup(org_grpid);// remove group users
			groupdao.delete(org_grpid);// remove group
			orguserdao.deleteByOrgHier(orgid); // remove org-node users
			
			return orghierdao.delete(orgid) >0;
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.delete.with", dae, "Org hierarchy", orgid);
		}
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public void addOrgHierMember(ServiceContext<?> svcctx, InfoId<Long> orgid, String... accounts)
			throws ServiceException {
		
		try{
			OrgHierInfo orginfo = orghierdao.query(orgid);
			
			InfoId<Long> groupId = IdKey.GROUP.getInfoId(orginfo.getGroupId());
			if(!InfoId.isValid(groupId))
				throw new ServiceException("excp.invld.id", groupId);
			
			for(String account: accounts){
				InfoId<Long> gid = IdKey.GROUP.getInfoId(orginfo.getGroupId());
				if(groupuserdao.existByAccount(gid, account)){
					continue;
				}
				GroupUserInfo  guinfo = new GroupUserInfo();
				InfoId<Long> rid = idservice.generateId(IdKey.GROUP_USER, Long.class);
				guinfo.setInfoId(rid);
				guinfo.setGroupId(orginfo.getGroupId());
				guinfo.setWorkgroupId(GeneralConstants.ORGHIER_WORKGROUP);
				guinfo.setAccount(account);				
				svcctx.setTraceInfo(guinfo);
				
				OrgUserInfo oui = new OrgUserInfo();
				InfoId<Long> ouid = idservice.generateId(IdKey.ORG_USER, Long.class);
				oui.setInfoId(ouid);
				oui.setAccount(account);
				oui.setOrgId(orgid.getId());				
				svcctx.setTraceInfo(oui);
				
				orguserdao.create(oui);
				groupuserdao.create(guinfo);
	
			}
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.add.mbr", dae, "org hierarchy", Arrays.toString(accounts));
		}

	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public void removeOrgHierMember(ServiceContext<?> svcctx, InfoId<Long> orgid, String... accounts)
			throws ServiceException {
		
		try{
			OrgHierInfo orginfo = orghierdao.query(orgid);
			
			InfoId<Long> groupId = IdKey.GROUP.getInfoId(orginfo.getGroupId());
			if(!InfoId.isValid(groupId))
				throw new ServiceException("excp.invld.id", groupId);
			
			for(String account: accounts){
				
				orguserdao.deleteByAccount(orgid, account);				
				groupuserdao.deleteByAccount(groupId, account);
			}
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.remove.mbr", dae, "org hierarchy", orgid);
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public List<UserInfo> getOrgHierMembers(ServiceContext<?> svcctx, InfoId<Long> orgid)
			throws ServiceException {
		
		List<UserInfo> rtv = null;
		OrgHierInfo orginfo = orghierdao.query(orgid);
		StringBuffer SQL = new StringBuffer();
		SQL.append("SELECT b.* FROM gp_users b, gp_org_user a ")
			.append("WHERE a.account = b.account ")
			.append(" AND a.org_id = ?");
		
		Object[] params = new Object[]{orginfo.getInfoId().getId()};
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / PARAMS : " + ArrayUtils.toString(params));
		}
		try{
			rtv = jtemplate.query(SQL.toString(), params, userdao.getRowMapper());	
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "org hierarchy", orgid);
		}

		return rtv;

	}
}
