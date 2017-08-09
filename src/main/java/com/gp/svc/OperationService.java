package com.gp.svc;

import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.dao.info.OperationInfo;
import com.gp.info.InfoId;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;

public interface OperationService {

	/**
	 * Get the activity logs of workgroup
	 * @param workgroupId the id of workgroup 
	 **/ 
	public PageWrapper<OperationInfo> getWorkgroupOperations(ServiceContext svcctx,
														 InfoId<Long> wid,
														 PageQuery pagequery) throws ServiceException;
	
	/**
	 * Get the activity logs of user
	 * @param account the user account
	 **/ 
	public PageWrapper<OperationInfo> getAccountOperations(ServiceContext svcctx,
													   String account,
													   PageQuery pagequery) throws ServiceException;
	
	/**
	 * Get the activity logs of object
	 * @param objectId the id of object 
	 **/ 
	public PageWrapper<OperationInfo> getObjectOperations(ServiceContext svcctx,
													  InfoId<?> objectId,
													  PageQuery pagequery) throws ServiceException;

	/**
	 * Add the activity log into database
	 **/
	public void addOperation(ServiceContext svcctx, OperationInfo activitylog) throws ServiceException;
}
