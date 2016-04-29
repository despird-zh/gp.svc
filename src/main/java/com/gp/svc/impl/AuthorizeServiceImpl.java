package com.gp.svc.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gp.dao.AuditDAO;
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
	
	@Override
	public List<RoleInfo> getAllRoles() throws ServiceException {
		
		return roledao.queryAll();
	}

	@Override
	public List<RolePageInfo> getRolePages(InfoId<Integer> roleId) throws ServiceException {
		
		StringBuffer SQL = new StringBuffer("");
		
		return null;
	}

	@Override
	public boolean setPagePerms(InfoId<Integer> roleId, InfoId<Integer> pageId, Map<FlatColLocator, Boolean> perms)
			throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

}
