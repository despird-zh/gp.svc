package com.gp.svc;

import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.dao.info.OperLogInfo;
import com.gp.info.InfoId;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;

public interface OperLogService {

	/**
	 * Get the activity logs of workgroup
	 * @param workgroupId the id of workgroup 
	 **/ 
	public PageWrapper<OperLogInfo> getWorkgroupOperLogs(ServiceContext svcctx,
														 InfoId<Long> wid,
														 PageQuery pagequery) throws ServiceException;
	
	/**
	 * Get the activity logs of user
	 * @param account the user account
	 **/ 
	public PageWrapper<OperLogInfo> getAccountOperLogs(ServiceContext svcctx,
													   String account,
													   PageQuery pagequery) throws ServiceException;
	
	/**
	 * Get the activity logs of object
	 * @param objectId the id of object 
	 **/ 
	public PageWrapper<OperLogInfo> getObjectOperLogs(ServiceContext svcctx,
													  InfoId<?> objectId,
													  PageQuery pagequery) throws ServiceException;

	/**
	 * Add the activity log into database
	 **/
	public void addOperLog(ServiceContext svcctx, OperLogInfo activitylog) throws ServiceException;
}
