package com.gp.svc;

import java.util.List;

import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.info.OrgHierInfo;
import com.gp.info.UserInfo;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;

public interface OrgHierService {

	public List<OrgHierInfo> getOrgHierNodes(ServiceContext<?> svcctx, Long orgNodeId) throws ServiceException;
	
	public boolean newOrgHierNode(ServiceContext<?> svcctx, OrgHierInfo orginfo) throws ServiceException;
	
	public boolean saveOrgHierNode(ServiceContext<?> svcctx, OrgHierInfo orginfo) throws ServiceException;
	
	public boolean removeOrgHierNode(ServiceContext<?> svcctx, InfoId<Long> orgid) throws ServiceException;
	
	public OrgHierInfo getOrgHierNode(ServiceContext<?> svcctx, InfoId<Long> orgid) throws ServiceException;
	
	public void addOrgHierMember(ServiceContext<?> svcctx, InfoId<Long> orgid, String ... accounts) throws ServiceException;
	
	public void removeOrgHierMember(ServiceContext<?> svcctx, InfoId<Long> orgid, String ... accounts) throws ServiceException;
	
	public PageWrapper<UserInfo> getOrgHierMembers(ServiceContext<?> svcctx, InfoId<Long> orgid, PageQuery pquery) throws ServiceException;

}
