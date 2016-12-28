package com.gp.svc;

import java.util.List;
import java.util.Map;

import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.dao.info.OrgHierInfo;
import com.gp.dao.info.UserInfo;

public interface OrgHierService {

	/**
	 * Find the sub org hierarchy nodes under a node
	 * @param orgNodeId the id of org node. 
	 **/
	public List<OrgHierInfo> getOrgHierNodes(ServiceContext svcctx, InfoId<Long> orgNodeId) throws ServiceException;
	
	public boolean newOrgHierNode(ServiceContext svcctx, OrgHierInfo orginfo) throws ServiceException;
	
	public boolean saveOrgHierNode(ServiceContext svcctx, OrgHierInfo orginfo) throws ServiceException;
	
	public boolean removeOrgHierNode(ServiceContext svcctx, InfoId<Long> orgid) throws ServiceException;
	
	public OrgHierInfo getOrgHierNode(ServiceContext svcctx, InfoId<Long> orgid) throws ServiceException;
	
	public void addOrgHierMember(ServiceContext svcctx, InfoId<Long> orgid, String ... accounts) throws ServiceException;
	
	public void removeOrgHierMember(ServiceContext svcctx, InfoId<Long> orgid, String ... accounts) throws ServiceException;
	
	public List<UserInfo> getOrgHierMembers(ServiceContext svcctx, InfoId<Long> orgid) throws ServiceException;

	/**
	 * Find the grand son organization node count 
	 * @return the map of key = son org id / value the grand node count.
	 **/
	public Map<Long, Integer> getOrgHierGrandNodeCount(ServiceContext svcctx, InfoId<Long> orgid) throws ServiceException;

	/**
	 * Find the route string of a org hier node
	 * @return the route : 1-3-4-5- 
	 **/
	public String getOrgHierRoute(ServiceContext svcctx, InfoId<Long> orgid) throws ServiceException;
	
	/**
	 * Find the org hierarchy node list
	 **/
	public List<OrgHierInfo> getOrgHierNodes(ServiceContext svcctx, InfoId<?>... orgids) throws ServiceException;

}
