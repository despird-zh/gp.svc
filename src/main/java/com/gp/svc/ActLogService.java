package com.gp.svc;

import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.info.OperationLogInfo;
import com.gp.info.InfoId;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;

public interface ActLogService {

	/**
	 * Get the activity logs of workgroup
	 * @param workgroupId the id of workgroup 
	 **/ 
	public PageWrapper<OperationLogInfo> getWorkgroupActivityLogs(ServiceContext svcctx,
																  InfoId<Long> wid,
																  PageQuery pagequery) throws ServiceException;
	
	/**
	 * Get the activity logs of user
	 * @param account the user account
	 **/ 
	public PageWrapper<OperationLogInfo> getAccountActivityLogs(ServiceContext svcctx,
																String account,
																PageQuery pagequery) throws ServiceException;
	
	/**
	 * Get the activity logs of object
	 * @param objectId the id of object 
	 **/ 
	public PageWrapper<OperationLogInfo> getObjectActivityLogs(ServiceContext svcctx,
															   InfoId<?> objectId,
															   PageQuery pagequery) throws ServiceException;

	/**
	 * Add the activity log into database
	 **/
	public void addActivityLog(ServiceContext svcctx, OperationLogInfo activitylog) throws ServiceException;
}
