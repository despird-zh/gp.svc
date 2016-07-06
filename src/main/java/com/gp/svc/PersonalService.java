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
	
	public List<GroupMemberInfo> getGroupMembers(ServiceContext svcctx, String account, String ... grpTypes) throws ServiceException;
	
	public UserSumInfo getUserSummary(ServiceContext svcctx, String account) throws ServiceException;
}
