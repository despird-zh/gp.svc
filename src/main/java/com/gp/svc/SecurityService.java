package com.gp.svc;

import java.util.List;
import java.util.Set;

import com.gp.common.ServiceContext;
import com.gp.common.Users;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.info.UserExInfo;
import com.gp.info.UserInfo;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;

public interface SecurityService {
	
	public boolean existAccount(ServiceContext svcctx, String account) throws ServiceException;
	
	/**
	 * create new account, system will create public and private cabinets for user
	 * the capacity of cabinets will be pushed in service context, then be extract 
	 * during creating new account. 
	 **/
	public boolean newAccount(ServiceContext svcctx, UserInfo uinfo, Long pubcapacity, Long pricapacity) throws ServiceException;
	
	/**
	 * Update the user information
	 * @param uinfo the user information 
	 **/
	public int updateAccount(ServiceContext svcctx, UserInfo uinfo, Long pubcapacity, Long pricapacity) throws ServiceException;
	
	/**
	 * Create a new external account
	 * @param uinfo the user information 
	 **/
	public boolean newAccountExt(ServiceContext svcctx, UserInfo uinfo) throws ServiceException;
	
	/**
	 * Get the lite user account information
	 * 
	 * @param userId the userId, if userId is null, then utilize the account parameter
	 * @param account the account, i.e login 
	 * @param type the type of user: inner / ldap / external 
	 * 
	 **/
	public UserInfo getAccountLite(ServiceContext svcctx,InfoId<Long> userId, String account, String type) throws ServiceException;
		
	/**
	 * Get the full user account information
	 * 
	 * @param userId the userId, if userId is null, then utilize the account parameter
	 * @param account the account, i.e login 
	 * @param type the type of user: inner / ldap / external 
	 **/
	public UserExInfo getAccountFull(ServiceContext svcctx,InfoId<Long> userId, String account, String type) throws ServiceException;
		
	/**
	 * Query the account list 
	 * 
	 * @param accountname the account or name as query condition
	 * @param instanceId the id of instance
	 * @param type the types condition
	 * @param state the states condition
	 **/
	public List<UserExInfo> getAccounts(ServiceContext svcctx, String accountname, Integer instanceId, String[] type,String[] state) throws ServiceException;

	/**
	 * Query the account list per page support pagination request
	 * 
	 * @param accountname the account or name as query condition
	 * @param instanceId the id of instance
	 * @param type the types condition
	 * @param pagequery the page query condition
	 *  
	 **/
	public PageWrapper<UserExInfo> getAccounts(ServiceContext svcctx, String accountname, Integer instanceId, String[] type, PageQuery pagequery) throws ServiceException;
	
	/**
	 * Query Roles of account in Greoupress System
	 * 
	 * @param wgroupId the work group id
	 * @param account the account i.e login
	 **/
	public Set<String> getAccountRoles(ServiceContext svcctx, InfoId<Long> wgroupId, String account) throws ServiceException;

	/**
	 * Query Groups of account in a work group
	 * 
	 * @param wgroupId the work group id
	 * @param account the account i.e login
	 **/
	public Set<String> getAccountGroups(ServiceContext svcctx, InfoId<Long> wgroupId, String account) throws ServiceException;
	
	/**
	 * Remove the account information
	 * @param userId the id of user
	 * @param account the account i.e login 
	 **/
	public boolean removeAccount(ServiceContext svcctx, InfoId<Long> userId, String account) throws ServiceException;

	/**
	 * Change the account password
	 * @param account the account i.e login 
	 * @param password the password of account
	 **/
	public boolean changePassword(ServiceContext svcctx, String account, String password) throws ServiceException;

	/**
	 * Change the account state
	 * @param userId the id of user
	 * @param state the state of account
	 **/
	public boolean changeAccountState(ServiceContext svcctx, InfoId<Long> userId, Users.UserState state) throws ServiceException;
	
	/**
	 * Update the Logon trace information
	 * @param userId the id of user
	 * @param resetRetry flag to resetRetry :true - reset to normal ; false count retry times
	 **/
	public boolean updateLogonTrace(ServiceContext svcctx, InfoId<Long> userId,boolean resetRetry)throws ServiceException;
}
