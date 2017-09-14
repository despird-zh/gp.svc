package com.gp.svc.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.common.FlatColumns;
import com.gp.common.GroupUsers;
import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.common.ServiceContext;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.common.DataSourceHolder;
import com.gp.dao.GroupDAO;
import com.gp.dao.GroupUserDAO;
import com.gp.dao.OrgHierDAO;
import com.gp.dao.PseudoDAO;
import com.gp.exception.ServiceException;
import com.gp.dao.info.GroupInfo;
import com.gp.dao.info.GroupUserInfo;
import com.gp.info.InfoId;
import com.gp.dao.info.OrgHierInfo;
import com.gp.svc.CommonService;
import com.gp.svc.OrgHierService;
import com.gp.svc.SecurityService;
import com.gp.svc.info.UserLiteInfo;

@Service
public class OrgHierServiceImpl implements OrgHierService{

	Logger LOGGER = LoggerFactory.getLogger(OrgHierServiceImpl.class);

	@Autowired
	private PseudoDAO pseudodao;
	
	@Autowired
	private OrgHierDAO orghierdao;

	@Autowired
	private GroupDAO groupdao;
	
	@Autowired
	private GroupUserDAO groupuserdao;
	
	@Autowired
	private CommonService idservice;

	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public List<OrgHierInfo> getOrgHierNodes(ServiceContext svcctx, InfoId<Long> parentNodeId)
			throws ServiceException {

		List<OrgHierInfo> orglist = null;
		
		StringBuffer SQL = new StringBuffer("select * from gp_org_hier where org_pid = ?");
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + parentNodeId);
		}
		try{
			
			orglist = jtemplate.query(SQL.toString(), new Object[]{parentNodeId.getId()}, OrgHierDAO.OrgHierMapper);
		
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with", dae, "Org hier", parentNodeId);
		}
		
		return orglist;
	}

	@Transactional(DataSourceHolder.TRNS_MGR)
	@Override
	public boolean newOrgHierNode(ServiceContext svcctx, OrgHierInfo orginfo) throws ServiceException {
		
		svcctx.setTraceInfo(orginfo);
		// prepare the group information
		GroupInfo ginfo = new GroupInfo();
		InfoId<Long> gid = idservice.generateId(IdKey.GP_GROUPS, Long.class);
		ginfo.setInfoId(gid);
		
		ginfo.setGroupName(orginfo.getOrgName() + "'s group");
		ginfo.setDescription(orginfo.getDescription());
		ginfo.setGroupType(GroupUsers.GroupType.ORG_HIER_MBR.name());
		// set trace information
		svcctx.setTraceInfo(ginfo);
		// create group user record
		GroupUserInfo mbrinfo= new GroupUserInfo();
		InfoId<Long> guid = idservice.generateId(IdKey.GP_GROUP_USER, Long.class);
		mbrinfo.setInfoId(guid);
		mbrinfo.setAccount(orginfo.getAdmin());
		mbrinfo.setGroupId(gid.getId());
		mbrinfo.setRole(GroupUsers.OrgHierMemberRole.MANAGER.name());
		svcctx.setTraceInfo(mbrinfo);
		try{
			// org group with orghier id
			ginfo.setManageId(orginfo.getInfoId().getId());
			// create group
			groupdao.create(ginfo);
			// create group user
			groupuserdao.create(mbrinfo);
			// update org hier info with group id
			orginfo.setMemberGroupId(gid.getId());
			
			return orghierdao.create(orginfo) > 0;
		
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.create", dae, "Org hierarchy");
		}
	}

	@Transactional(DataSourceHolder.TRNS_MGR)
	@Override
	public boolean saveOrgHierNode(ServiceContext svcctx, OrgHierInfo orginfo) throws ServiceException {
		try{
			
			svcctx.setTraceInfo(orginfo);
			return orghierdao.update(orginfo,FilterMode.NONE) > 0;
		
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.update", dae, "org hierarchy");
		}
	}

	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public OrgHierInfo getOrgHierNode(ServiceContext svcctx, InfoId<Long> orgid) throws ServiceException {
		try{

			return orghierdao.query(orgid);
		
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with", dae, "org hierarchy", orgid);
		}
	}
	
	@Transactional(DataSourceHolder.TRNS_MGR)
	@Override
	public boolean removeOrgHierNode(ServiceContext svcctx, InfoId<Long> orgid) throws ServiceException {

		try{
			OrgHierInfo orginfo = orghierdao.query(orgid);
			InfoId<Long> org_grpid = IdKeys.getInfoId(IdKey.GP_GROUPS, orginfo.getMemberGroupId());
			groupuserdao.deleteByGroup(org_grpid);// remove group users
			groupdao.delete(org_grpid);// remove group

			return orghierdao.delete(orgid) >0;
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.delete.with", dae, "Org hierarchy", orgid);
		}
	}

	@Transactional(DataSourceHolder.TRNS_MGR)
	@Override
	public void addOrgHierMember(ServiceContext svcctx, InfoId<Long> orgid, String... accounts)
			throws ServiceException {
		
		try{
			OrgHierInfo orginfo = orghierdao.query(orgid);
			
			InfoId<Long> groupId = IdKeys.getInfoId(IdKey.GP_GROUPS, orginfo.getMemberGroupId());
			if(!IdKeys.isValidId(groupId))
				throw new ServiceException("excp.invld.id", groupId);
			
			for(String account: accounts){
				InfoId<Long> gid = IdKeys.getInfoId(IdKey.GP_GROUPS, orginfo.getMemberGroupId());
				InfoId<Long> mbrid = groupuserdao.existByAccount(gid, account);
				if(IdKeys.isValidId(mbrid)){
					continue;
				}
				GroupUserInfo  guinfo = new GroupUserInfo();
				InfoId<Long> rid = idservice.generateId(IdKey.GP_GROUP_USER, Long.class);
				guinfo.setInfoId(rid);
				guinfo.setGroupId(orginfo.getMemberGroupId());
				guinfo.setRole(GroupUsers.OrgHierMemberRole.MEMBER.name());
				guinfo.setAccount(account);				
				svcctx.setTraceInfo(guinfo);
				
				groupuserdao.create(guinfo);
	
			}
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.add.mbr", dae, "org hierarchy", Arrays.toString(accounts));
		}

	}

	@Transactional(DataSourceHolder.TRNS_MGR)
	@Override
	public void removeOrgHierMember(ServiceContext svcctx, InfoId<Long> orgid, String... accounts)
			throws ServiceException {
		
		try{
			OrgHierInfo orginfo = orghierdao.query(orgid);

			InfoId<Long> groupId = IdKeys.getInfoId(IdKey.GP_GROUPS, orginfo.getMemberGroupId());
			if(!IdKeys.isValidId(groupId))
				throw new ServiceException("excp.invld.id", groupId);
			
			for(String account: accounts){
	
				groupuserdao.deleteByAccount(groupId, account);
			}
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.remove.mbr", dae, "org hierarchy", orgid);
		}
	}

	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public List<UserLiteInfo> getOrgHierMembers(ServiceContext svcctx, InfoId<Long> orgid)
			throws ServiceException {
		
		List<UserLiteInfo> rtv = null;
		Long memberGroupId = pseudodao.query(orgid, FlatColumns.MBR_GRP_ID, Long.class);
	
		StringBuffer SQL = new StringBuffer();
		
		SQL.append("SELECT b.user_id, b.account, b.email, b.full_name, b.mobile, b.type, b.create_time, b.state, ")
			.append(" i.image_link,")
			.append(" s.source_id, s.source_name, s.abbr ")
			.append("FROM gp_users b, gp_group_user a, gp_images i, gp_sources s ")
			.append("WHERE a.account = b.account ")
			.append("AND b.source_id = s.source_id ")
			.append("AND b.avatar_id = i.image_id ")
			.append("AND a.group_id = ?");
		
		Object[] params = new Object[]{ memberGroupId };
		
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / PARAMS : " + ArrayUtils.toString(params));
		}
		try{
			rtv = jtemplate.query(SQL.toString(), params, SecurityService.USER_LITE_ROW_MAPPER);	
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "org hierarchy", orgid);
		}

		return rtv;

	}

	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public Map<Long, Integer> getOrgHierGrandNodeCount(ServiceContext svcctx, InfoId<Long> orgid)
			throws ServiceException {
		
		final Map<Long, Integer> result = new HashMap<Long, Integer> ();
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("select count(org_id) as grand_cnt, org_pid from gp_org_hier ");
		SQL.append(" where org_pid in (select org_id from gp_org_hier where org_pid = ?)");
		SQL.append(" group by org_pid");
		
		Object[] params = new Object[]{ orgid.getId() };
		
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / PARAMS : " + ArrayUtils.toString(params));
		}
		try{
			jtemplate.query(SQL.toString(), params, new RowCallbackHandler(){

				@Override
				public void processRow(ResultSet rs) throws SQLException {
					result.put(rs.getLong("org_pid"), rs.getInt("grand_cnt"));
				}});	
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "grand's count", orgid);
		}

		return result;
	}

	@Override
	public String getOrgHierRoute(ServiceContext svcctx, InfoId<Long> orgid) throws ServiceException {
		
		SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_org_id", orgid.getId());
		
	    SimpleJdbcCall jdbcCall = pseudodao.getJdbcCall("proc_org_route");
	    String routes = null;
	    try{
			Map<String, Object> out = jdbcCall.execute(in);
			routes = (String)out.get("route_ids");
	    }catch(DataAccessException dae){
	    	throw new ServiceException("excp.proc.with",dae, "proc_org_route", orgid);
	    }
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("call procedure: proc_org_route / params : p_org_id-{}", orgid.getId());
		}
		return routes;
	}

	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public List<OrgHierInfo> getOrgHierNodes(ServiceContext svcctx, InfoId<?>... orgids) throws ServiceException {
		
		try{
			
			return orghierdao.queryByIds(orgids);
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query", dae, "org hier");
		}

	}

	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public List<OrgHierInfo> getOrgHierAllNodes(ServiceContext svcctx, InfoId<Long> orgNodeId) throws ServiceException {
		
		List<OrgHierInfo> all = new ArrayList<OrgHierInfo>();
		List<InfoId<Long>> ids = new ArrayList<InfoId<Long>>();
		try{
			
			List<OrgHierInfo> lvl1 = orghierdao.querySubByIds(orgNodeId);
			all.addAll(lvl1);
			for(OrgHierInfo oinfo: lvl1){
				ids.add(oinfo.getInfoId());
			}
			List<OrgHierInfo> subs = getOrgHierSubNodes(svcctx, ids.toArray(new InfoId[0]));
			if(!subs.isEmpty()) all.addAll(subs);
			return all;
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query", dae, "org hier");
		}
	}
	
	private List<OrgHierInfo> getOrgHierSubNodes(ServiceContext svcctx, InfoId<?> ...orgNodeIds) throws ServiceException {
		
		List<OrgHierInfo> all = new ArrayList<OrgHierInfo>();
		List<InfoId<Long>> ids = new ArrayList<InfoId<Long>>();
		try{
			
			List<OrgHierInfo> lvl1 = orghierdao.querySubByIds(orgNodeIds);
			all.addAll(lvl1);
			for(OrgHierInfo oinfo: lvl1){
				ids.add(oinfo.getInfoId());
			}
			if(!ids.isEmpty()){
				List<OrgHierInfo> subs = getOrgHierSubNodes(svcctx, ids.toArray(new InfoId[0]));
				if(!subs.isEmpty()) 
					all.addAll(subs);
			}
			return all;
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query", dae, "org hier");
		}
	}
}
