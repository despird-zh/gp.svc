package com.gp.svc.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.config.ServiceConfigurer;
import com.gp.dao.PageDAO;
import com.gp.dao.PseudoDAO;
import com.gp.dao.RoleDAO;
import com.gp.dao.RolePageDAO;
import com.gp.exception.ServiceException;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.dao.info.RoleInfo;
import com.gp.dao.info.RolePageInfo;
import com.gp.svc.AuthorizeService;

@Service
public class AuthorizeServiceImpl implements AuthorizeService{

	Logger LOGGER = LoggerFactory.getLogger(AuthorizeServiceImpl.class);
	
	@Autowired
	RoleDAO roledao;
	
	@Autowired
	RolePageDAO rolepagedao;
	
	@Autowired
	PseudoDAO pseudodao;
	
	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public List<RoleInfo> getAllRoles() throws ServiceException {
		
		return roledao.queryAll();
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public List<RolePageInfo> getRolePages(InfoId<Integer> roleId) throws ServiceException {
		
		StringBuffer SQL = new StringBuffer("SELECT * FROM gp_role_page WHERE role_id = ?");
		
		Object[] params = new Object[]{roleId.getId()};
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL.toString(), ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		
		try{
			return jtemplate.query(SQL.toString(), params, RolePageDAO.RolePageMapper);
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae,"RolePage", roleId);
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean setPagePerms(InfoId<Integer> roleId, InfoId<Integer> pageId, Map<FlatColLocator, Boolean> perms)
			throws ServiceException {
		try{
			return rolepagedao.update(roleId, pageId, perms) > 0;
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae,"RolePage",  roleId + "/" + pageId);
		}
	}

	@Override
	public List<DefaultKeyValue> getRolePagePerms(String pageName, String role) throws ServiceException{
		
		StringBuffer SQL = new StringBuffer("SELECT rp.*, r.*,p.* ");
		SQL.append("FROM gp_role_page rp ");
		SQL.append("LEFT JOIN gp_roles r on rp.role_id = r.role_id ");
		SQL.append("LEFT JOIN gp_pages p on rp.page_id = p.page_id ");
		SQL.append("WHERE 1=1 ");
		
		ArrayList<Object> params = new ArrayList<Object>();
		if(StringUtils.isBlank(pageName)){
			SQL.append("AND p.page_name = ? ");
			params.add(pageName);
		}
		if(StringUtils.isBlank(role)){
			SQL.append("AND r.role_name = ? ");
			params.add(role);
		}
		
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		List<DefaultKeyValue> rtv = new ArrayList<DefaultKeyValue>();
		try{
			jtemplate.query(SQL.toString(),params.toArray(), new RowCallbackHandler(){

				@Override
				public void processRow(ResultSet rs) throws SQLException {
					DefaultKeyValue kv = null;
					String pageUrl = rs.getString("page_url");
					String roleAbbr = rs.getString("role_abbr");
					
					for(int i = 1; i <= PageDAO.COLUMN_COUNT ; i++){
						
						String perm = rs.getString("act_abbr_" + i);
						boolean flag = rs.getBoolean("act_perm_" + i);
						
						if(StringUtils.isNotBlank(perm) && flag){
							kv = new DefaultKeyValue();
							
							kv.setKey(pageUrl);
							kv.setValue(roleAbbr + ":" + perm);
							
							rtv.add(kv);
						}
					}
				}});
			
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query", dae,"RolePage");
		}
		
		return rtv;
	}

}
