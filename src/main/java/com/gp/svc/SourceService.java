package com.gp.svc;

import java.util.List;
import java.util.Map;

import com.gp.common.Sources.State;
import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.dao.info.SourceInfo;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;

public interface SourceService {

	/**
	 * Get the source information by id 
	 **/
	public SourceInfo getSource(ServiceContext svcctx,InfoId<Integer> id) throws ServiceException;
	
	/**
	 * Get source information by entity and node keys 
	 **/
	public SourceInfo getSource(ServiceContext svcctx, String entity, String node) throws ServiceException;
	
	/**
	 * Save the source information 
	 **/
	public boolean saveSource(ServiceContext svcctx, SourceInfo source) throws ServiceException;

	/**
	 * Add external source information into system 
	 **/
	public boolean addExtSource(ServiceContext svcctx, SourceInfo source) throws ServiceException;

	/**
	 * Support for full query without paging process on server-side 
	 **/
	public List<SourceInfo> getSources(ServiceContext svcctx, String sourcename) throws ServiceException;

	/**
	 * Support for server-side paging process 
	 **/
	public PageWrapper<SourceInfo> getSources(ServiceContext svcctx, String sourcename, PageQuery pquery) throws ServiceException;

	/**
	 * Change the state of source 
	 **/
	public boolean changeSourceState(ServiceContext svcctx, InfoId<Integer> sourceId, State state) throws ServiceException;
	
	
	public Map<String, SourceInfo> getAccountSources(ServiceContext svcctx, List<String> accounts) throws ServiceException;
	
}
