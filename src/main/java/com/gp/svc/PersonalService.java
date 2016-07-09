package com.gp.svc;

import java.util.List;

import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.info.GroupMemberInfo;
import com.gp.info.InfoId;
import com.gp.info.OrgHierInfo;
import com.gp.info.UserSumInfo;
import com.gp.info.WorkgroupInfo;

public interface PersonalService {

	public List<WorkgroupInfo> getWorkgroups(ServiceContext svcctx, InfoId<?>... ids)throws ServiceException;
	
	public List<OrgHierInfo> getOrgHierNodes(ServiceContext svcctx, InfoId<?>... ids)throws ServiceException;
	
	/**
	 * Get the work group member belonging relationship by account.
	 * this helps to find the work group belongs of a user.
	 * 
	 * @param account the account information
	 **/
	public List<GroupMemberInfo> getWorkgroupMembers(ServiceContext svcctx, String account) throws ServiceException;
	/**
	 * Get the org hier member belonging relationship by account
	 * this helps to find the org hier belongs of a user.
	 * 
	 * @param account the account information
	 **/
	public List<GroupMemberInfo> getOrgHierMembers(ServiceContext svcctx, String account) throws ServiceException;
	
	public UserSumInfo getUserSummary(ServiceContext svcctx, String account) throws ServiceException;

	//public List<MessageInfo> getMessage
}
