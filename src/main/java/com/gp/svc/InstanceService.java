package com.gp.svc;

import java.util.List;
import java.util.Map;

import com.gp.common.Instances.State;
import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.info.InstanceInfo;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;

public interface InstanceService {

	/**
	 * Get the instance information by id 
	 **/
	public InstanceInfo getInstnaceInfo(ServiceContext svcctx,InfoId<Integer> id) throws ServiceException;
	
	/**
	 * Get instance information by entity and node keys 
	 **/
	public InstanceInfo getInstnace(ServiceContext svcctx, String entity, String node) throws ServiceException;
	
	/**
	 * Save the instance information 
	 **/
	public boolean saveInstnace(ServiceContext svcctx, InstanceInfo instance) throws ServiceException;

	/**
	 * Add external instance information into system 
	 **/
	public boolean addExtInstnace(ServiceContext svcctx, InstanceInfo instance) throws ServiceException;

	/**
	 * Support for full query without paging process on server-side 
	 **/
	public List<InstanceInfo> getInstances(ServiceContext svcctx, String instancename) throws ServiceException;

	/**
	 * Support for server-side paging process 
	 **/
	public PageWrapper<InstanceInfo> getInstances(ServiceContext svcctx, String instancename, PageQuery pquery) throws ServiceException;

	/**
	 * Change the state of instance 
	 **/
	public boolean changeInstanceState(ServiceContext svcctx, InfoId<Integer> instanceId, State state) throws ServiceException;
	
	
	public Map<String, InstanceInfo> getAccountSources(ServiceContext svcctx, List<String> accounts) throws ServiceException;
	
}
