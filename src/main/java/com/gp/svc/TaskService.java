package com.gp.svc;

import java.util.Date;
import java.util.List;

import com.gp.common.ServiceContext;
import com.gp.common.Tasks;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.info.TaskInfo;
import com.gp.info.TaskRouteInfo;

public interface TaskService {
	
	/**
	 * Get all the tasks data of specified workgroup
	 * 
	 * @param workgroupKey the workgroup key
	 * @param state the task state
	 * @param fromDate the query start date
	 * 
	 **/
	public List<TaskInfo> getWorkgrupTasks(ServiceContext<?> svcctx, InfoId<Long> workgroupKey ,Tasks.TaskState state,  Date fromDate) throws ServiceException;
	
	/**
	 * Get complete tasks of specified person
	 * 
	 * @param account the account
	 * @param state the task state
	 * @param fromDate the query start date
	 * 
	 **/
	public List<TaskInfo> getPersonalTasks(ServiceContext<?> svcctx, String account, Tasks.TaskState state,  Date fromDate) throws ServiceException;

	/**
	 * Create a new task information
	 * 
	 * @param task the task information
	 * @return InfoId<Long> the task key
	 **/
	public InfoId<Long> newTask(ServiceContext<?> svcctx, TaskInfo task) throws ServiceException;
	
	/**
	 * Remove the task information
	 * 
	 * 
	 **/
	public void remoteTask(ServiceContext<?> svcctx, InfoId<Long> taskKey) throws ServiceException;
	
	public TaskInfo getTask(ServiceContext<?> svcctx, InfoId<Long> workgroupKey, InfoId<Long> taskKey) throws ServiceException;
	
	public List<TaskRouteInfo> getTaskRoutes(ServiceContext<?> svcctx, InfoId<Long> workgroupKey, InfoId<Long> taskKey) throws ServiceException;
	
	public InfoId<Long> forwardTask(ServiceContext<?> svcctx, InfoId<Long> workgroupKey, InfoId<Long> taskKey,String nextExecutor) throws ServiceException;
	
}
