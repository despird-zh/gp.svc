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
	
	/**
	 * Create a org hier node under certain node 
	 **/
	public boolean newOrgHierNode(ServiceContext svcctx, OrgHierInfo orginfo) throws ServiceException;
	
	/**
	 * Save the information of ORG hierarchy node 
	 **/
	public boolean saveOrgHierNode(ServiceContext svcctx, OrgHierInfo orginfo) throws ServiceException;
	
	/**
	 * Remove the ORG hierarchy node 
	 **/
	public boolean removeOrgHierNode(ServiceContext svcctx, InfoId<Long> orgid) throws ServiceException;
	
	/**
	 * Get certain ORG hierarchy node information 
	 **/
	public OrgHierInfo getOrgHierNode(ServiceContext svcctx, InfoId<Long> orgid) throws ServiceException;
	
	/**
	 * Add ORG hierarchy node member 
	 **/
	public void addOrgHierMember(ServiceContext svcctx, InfoId<Long> orgid, String ... accounts) throws ServiceException;
	
	/**
	 * Remove member out of an ORG hierarchy node 
	 **/
	public void removeOrgHierMember(ServiceContext svcctx, InfoId<Long> orgid, String ... accounts) throws ServiceException;
	
	/**
	 * Get all members of an ORG hierarchy node 
	 **/
	public List<UserInfo> getOrgHierMembers(ServiceContext svcctx, InfoId<Long> orgid) throws ServiceException;

	/**
	 * Find the grand son organization node count 
	 * @return the map of key = son ORG id / value the grand node count.
	 **/
	public Map<Long, Integer> getOrgHierGrandNodeCount(ServiceContext svcctx, InfoId<Long> orgid) throws ServiceException;

	/**
	 * Find the route string of an ORG hierarchy node
	 * @return the route : 1-3-4-5- 
	 **/
	public String getOrgHierRoute(ServiceContext svcctx, InfoId<Long> orgid) throws ServiceException;
	
	/**
	 * Find the org hierarchy node list
	 **/
	public List<OrgHierInfo> getOrgHierNodes(ServiceContext svcctx, InfoId<?>... orgids) throws ServiceException;

	/**
	 * Find all the sub nodes of an ORG hierarchy node
	 **/
	public List<OrgHierInfo> getOrgHierAllNodes(ServiceContext svcctx, InfoId<Long> orgNodeId) throws ServiceException;
}
