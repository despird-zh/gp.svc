package com.gp.svc.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.common.IdKey;
import com.gp.common.ServiceContext;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.OrgHierDAO;
import com.gp.dao.PseudoDAO;
import com.gp.dao.UserSumDAO;
import com.gp.dao.WorkgroupDAO;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.info.OrgHierInfo;
import com.gp.info.UserSumInfo;
import com.gp.info.WorkgroupInfo;
import com.gp.svc.PersonalService;
import com.gp.svc.info.GroupMemberInfo;

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
	public List<WorkgroupInfo> getWorkgroups(ServiceContext svcctx, InfoId<?>... ids) throws ServiceException {
		
		try{
			return workgroupdao.queryByIds(ids);
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "workgroup", Arrays.toString(ids));
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public List<OrgHierInfo> getOrgHierNodes(ServiceContext svcctx, InfoId<?>... ids) throws ServiceException {

		try{
			return orghierdao.queryByIds(ids);
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "orghier", Arrays.toString(ids));
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public List<GroupMemberInfo> getGroupMembers(ServiceContext svcctx, String account, String... grpTypes)
			throws ServiceException {
		
		StringBuffer SQL = new StringBuffer("SELECT * FROM gp_group_mbrs ");
		SQL.append("WHERE account = :account AND group_type IN (:types)");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		params.put("types", Arrays.asList(grpTypes));
		
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}",SQL.toString(), params.toString());
		}

		try{
			return jtemplate.query(SQL.toString(), params, new RowMapper<GroupMemberInfo>(){

				@Override
				public GroupMemberInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
					GroupMemberInfo gminfo = new GroupMemberInfo();
					Long relid = rs.getLong("mbr_rel_id");
					InfoId<Long> rid = IdKey.GROUP_USER.getInfoId(relid);
					gminfo.setInfoId(rid);
					gminfo.setAccount(rs.getString("account"));
					gminfo.setDescription(rs.getString("group_descr"));
					gminfo.setRole(rs.getString("role"));
					gminfo.setGroupId(rs.getLong("group_id"));
					gminfo.setGroupName(rs.getString("group_name"));
					gminfo.setGroupType(rs.getString("group_type"));
					gminfo.setManageId(rs.getLong("manage_id"));
					
					return gminfo;
				}
			});
		
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with", dae, "group member", params.toString());
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

}
