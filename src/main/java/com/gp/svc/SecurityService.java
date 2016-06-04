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

	public static final String CTX_KEY_PUBCAPACITY = "_pub_capacity";
	public static final String CTX_KEY_PRICAPACITY = "_pri_capacity";
	
	public boolean existAccount(ServiceContext<?> svcctx, String account) throws ServiceException;
	
	/**
	 * create new account, system will create public and private cabinets for user
	 * the capacity of cabinets will be pushed in service context, then be extract 
	 * during creating new account. 
	 **/
	public boolean newAccount(ServiceContext<?> svcctx, UserInfo uinfo) throws ServiceException;
	
	public int updateAccount(ServiceContext<?> svcctx, UserInfo uinfo) throws ServiceException;
	
	public boolean newAccountExt(ServiceContext<?> svcctx, UserInfo uinfo) throws ServiceException;
	
	public UserInfo getAccountLite(ServiceContext<?> svcctx,InfoId<Long> userId, String account, String type) throws ServiceException;
		
	public UserExInfo getAccountFull(ServiceContext<?> svcctx,InfoId<Long> userId, String account, String type) throws ServiceException;
		
	public List<UserExInfo> getAccounts(ServiceContext<?> svcctx, String accountname, Integer instanceId, String[] type,String[] state) throws ServiceException;
	
	public List<UserExInfo> getAccounts(ServiceContext<?> svcctx, List<String> accounts) throws ServiceException;
	
	public PageWrapper<UserExInfo> getAccounts(ServiceContext<?> svcctx, String accountname, Integer instanceId, String[] type, PageQuery pagequery) throws ServiceException;
	
	public Set<String> getAccountRoles(ServiceContext<?> svcctx, InfoId<Long> wkey, String account) throws ServiceException;
	
	public Set<String> getAccountGroups(ServiceContext<?> svcctx, InfoId<Long> wkey, String account) throws ServiceException;
	
	public boolean removeAccount(ServiceContext<?> svcctx, InfoId<Long> userId, String account) throws ServiceException;
	
	public boolean changePassword(ServiceContext<?> svcctx, String account, String password) throws ServiceException;

	public boolean changeAccountState(ServiceContext<?> svcctx, InfoId<Long> userkey, Users.UserState state) throws ServiceException;
	
	public boolean updateLogonTrace(ServiceContext<?> svcctx, InfoId<Long> userkey,boolean resetRetry)throws ServiceException;
}
