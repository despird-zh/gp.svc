package com.gp.svc;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.keyvalue.DefaultKeyValue;

import com.gp.exception.ServiceException;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.dao.info.RoleInfo;
import com.gp.dao.info.RolePageInfo;

public interface AuthorizeService {

	/**
	 * Get all the roles defined
	 **/
	public List<RoleInfo> getAllRoles() throws ServiceException;
	
	/**
	 * Get the all the pages of a role
	 */
	public List<RolePageInfo> getRolePages(InfoId<Integer> roleId)throws ServiceException;
	
	/**
	 * Grant or revoke permission on page 
	 **/
	public boolean setPagePerms(InfoId<Integer> roleId,InfoId<Integer> pageId,  Map<FlatColLocator, Boolean> perms) throws ServiceException;

	/**
	 * Get all the role page perms
	 * @param pageName the page name
	 **/
	public List<DefaultKeyValue> getRolePagePerms(String pageName, String role)throws ServiceException;
}
