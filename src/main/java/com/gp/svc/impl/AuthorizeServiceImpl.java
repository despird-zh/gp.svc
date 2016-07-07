package com.gp.svc.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.config.ServiceConfigurer;
import com.gp.dao.PseudoDAO;
import com.gp.dao.RoleDAO;
import com.gp.dao.RolePageDAO;
import com.gp.exception.ServiceException;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.info.RoleInfo;
import com.gp.info.RolePageInfo;
import com.gp.svc.AuthorizeService;

@Service("authorizeService")
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

}
