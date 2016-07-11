package com.gp.svc.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.common.GroupUsers.GroupType;
import com.gp.common.IdKey;
import com.gp.common.ServiceContext;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.GroupUserDAO;
import com.gp.dao.OrgHierDAO;
import com.gp.dao.PseudoDAO;
import com.gp.dao.UserSumDAO;
import com.gp.dao.WorkgroupDAO;
import com.gp.exception.ServiceException;
import com.gp.info.ChatMessageInfo;
import com.gp.info.GroupMemberInfo;
import com.gp.info.InfoId;
import com.gp.info.OrgHierInfo;
import com.gp.info.UserSumInfo;
import com.gp.info.WorkgroupInfo;
import com.gp.pagination.PageQuery;
import com.gp.svc.PersonalService;

@Service("personalService")
public class PersonalServiceImpl implements PersonalService{

	static Logger LOGGER = LoggerFactory.getLogger(PersonalServiceImpl.class);
	
	@Autowired
	PseudoDAO pseudodao;
	
	@Autowired
	OrgHierDAO orghierdao;
	
	@Autowired
	WorkgroupDAO workgroupdao;
	
	@Autowired
	UserSumDAO usersumdao;
	
	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public List<WorkgroupInfo> getWorkgroups(ServiceContext svcctx, String account) throws ServiceException {
		
		StringBuffer SQL = new StringBuffer("SELECT * FROM gp_workgroup_mbrs ");
		SQL.append("WHERE account = ? AND group_type = '").append(GroupType.WORKGROUP_MBR.name()).append("'");
		
		Object[] params = new Object[]{
			account
		};

		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}",SQL.toString(), params.toString());
		}
		InfoId<?>[] ids = null;
		try{
			List<GroupMemberInfo> members = jtemplate.query(SQL.toString(), params, GroupUserDAO.GroupMemberMapper);
			ids = new InfoId<?>[members.size()];
			int count = 0;
			for(GroupMemberInfo minfo : members){
				ids[count] = IdKey.WORKGROUP.getInfoId(minfo.getManageId());
				count ++;
			}
			return workgroupdao.queryByIds(ids);
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "workgroup", Arrays.toString(ids));
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public List<OrgHierInfo> getOrgHierNodes(ServiceContext svcctx, String account) throws ServiceException {
		StringBuffer SQL = new StringBuffer("SELECT * FROM gp_orghier_mbrs ");
		SQL.append("WHERE account = ? AND group_type = '").append(GroupType.ORG_HIER_MBR.name()).append("'");
		
		Object[] params = new Object[]{
				account
			};
		
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}",SQL.toString(), params.toString());
		}
		InfoId<?>[] ids = null;
		try{
			List<GroupMemberInfo> members = jtemplate.query(SQL.toString(), params, GroupUserDAO.GroupMemberMapper);
			ids = new InfoId<?>[members.size()];
			int count = 0;
			for(GroupMemberInfo minfo : members){
				ids[count] = IdKey.WORKGROUP.getInfoId(minfo.getManageId());
				count ++;
			}
			
			return orghierdao.queryByIds(ids);
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "orghier", Arrays.toString(ids));
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public List<GroupMemberInfo> getWorkgroupMembers(ServiceContext svcctx, String account)
			throws ServiceException {
		
		StringBuffer SQL = new StringBuffer("SELECT * FROM gp_workgroup_mbrs ");
		SQL.append("WHERE account = :account AND group_type = '").append(GroupType.WORKGROUP_MBR.name()).append("'");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}",SQL.toString(), params.toString());
		}

		try{
			return jtemplate.query(SQL.toString(), params, GroupUserDAO.GroupMemberMapper);
		
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with", dae, "group member", params.toString());
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public List<GroupMemberInfo> getOrgHierMembers(ServiceContext svcctx, String account)
			throws ServiceException {
		
		StringBuffer SQL = new StringBuffer("SELECT * FROM gp_orghier_mbrs ");
		SQL.append("WHERE account = :account AND group_type = '").append(GroupType.ORG_HIER_MBR.name()).append("'");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}",SQL.toString(), params.toString());
		}

		try{
			return jtemplate.query(SQL.toString(), params, GroupUserDAO.GroupMemberMapper);
		
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with", dae, "orghier member", params.toString());
		}
	}
	
	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public UserSumInfo getUserSummary(ServiceContext svcctx, String account) throws ServiceException {
		
		try{
			
			return usersumdao.queryByAccount(account);
			
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "user sumamry", account);
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public List<ChatMessageInfo> getChatMessages(ServiceContext svcctx, String account, PageQuery pquery)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

}
