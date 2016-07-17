package com.gp.svc;

import java.util.List;
import java.util.Map;

import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.info.CombineInfo;
import com.gp.info.InfoId;
import com.gp.dao.info.ChatMessageInfo;
import com.gp.dao.info.GroupMemberInfo;
import com.gp.dao.info.MemberSettingInfo;
import com.gp.dao.info.OrgHierInfo;
import com.gp.dao.info.UserInfo;
import com.gp.dao.info.UserSumInfo;
import com.gp.dao.info.WorkgroupInfo;
import com.gp.pagination.PageQuery;

public interface PersonalService {

	/**
	 * Get the work groups which user joined, and the personal setting on workgroup
	 * @param account the account information
	 **/
	public List<CombineInfo<WorkgroupInfo, Boolean>> getWorkgroups(ServiceContext svcctx, String account)throws ServiceException;
	
	/**
	 * Get the orghier which user joined
	 * @param account the account information
	 **/
	public List<CombineInfo<OrgHierInfo, Boolean>> getOrgHierNodes(ServiceContext svcctx, String account)throws ServiceException;
	
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

	/**
	 * Update the user basic setting 
	 **/
	public boolean updateBasicSetting(ServiceContext svcctx, UserInfo uinfo, String avatarImg)throws ServiceException;

	/**
	 * Update the belong setting 
	 **/
	public boolean updateBelongSetting(ServiceContext svcctx, String account,Map<InfoId<Long>, Boolean> settings)throws ServiceException;
	
}
