package com.gp.svc;

import java.util.List;

import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.info.ChatMessageInfo;
import com.gp.info.GroupMemberInfo;
import com.gp.info.OrgHierInfo;
import com.gp.info.UserInfo;
import com.gp.info.UserSumInfo;
import com.gp.info.WorkgroupInfo;
import com.gp.pagination.PageQuery;

public interface PersonalService {

	/**
	 * Get the work groups which user joined
	 * @param account the account information
	 **/
	public List<WorkgroupInfo> getWorkgroups(ServiceContext svcctx, String account)throws ServiceException;
	
	/**
	 * Get the orghier which user joined
	 * @param account the account information
	 **/
	public List<OrgHierInfo> getOrgHierNodes(ServiceContext svcctx, String account)throws ServiceException;
	
	/**
	 * Get the work group member belonging relationship by account.
	 * this helps to find the work group belongs of a user.
	 * @param account the account information
	 **/
	public List<GroupMemberInfo> getWorkgroupMembers(ServiceContext svcctx, String account) throws ServiceException;
	
	/**
	 * Get the org hier member belonging relationship by account
	 * this helps to find the org hier belongs of a user.
	 * @param account the account information
	 **/
	public List<GroupMemberInfo> getOrgHierMembers(ServiceContext svcctx, String account) throws ServiceException;
	
	/**
	 * Get the user summary information
	 * @param account the account information 
	 **/
	public UserSumInfo getUserSummary(ServiceContext svcctx, String account) throws ServiceException;
	
	/**
	 * Get the user summary information
	 * @param account the account information 
	 **/
	public List<ChatMessageInfo> getChatMessages(ServiceContext svcctx, String account, PageQuery pquery)throws ServiceException;

	
	public boolean updateBasicSetting(ServiceContext svcctx, UserInfo uinfo, String avatarImg)throws ServiceException;
}
