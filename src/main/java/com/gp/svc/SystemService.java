package com.gp.svc;

import java.util.List;

import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.dao.info.SysOptionInfo;
import com.gp.dao.info.TokenInfo;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;

public interface SystemService {

	public List<SysOptionInfo> getOptions(ServiceContext svcctx) throws ServiceException;
	
	public List<SysOptionInfo> getOptions(ServiceContext svcctx, String groupKey) throws ServiceException;
	
	public PageWrapper<SysOptionInfo> getOptions(ServiceContext svcctx, String groupKey, PageQuery pagequery) throws ServiceException;
	
	public boolean updateOption(ServiceContext svcctx, String key, String value) throws ServiceException;
	
	public boolean updateOption(ServiceContext svcctx, InfoId<Long> oKey, String value) throws ServiceException;
	
	public SysOptionInfo getOption(ServiceContext svcctx, String optKey) throws ServiceException;

	public SysOptionInfo getOption(ServiceContext svcctx, InfoId<Long> oKey) throws ServiceException;

	public List<String> getOptionGroups(ServiceContext svcctx) throws ServiceException;
	
	/**
	 * get a token from database 
	 **/
	public TokenInfo getToken(ServiceContext svcctx, InfoId<Long> tokenKey) throws ServiceException;
	
	/**
	 * new a token in database 
	 **/
	public boolean newToken(ServiceContext svcctx, TokenInfo token) throws ServiceException;
}
