package com.gp.svc.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gp.common.ServiceContext;
import com.gp.common.Tasks.TaskState;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.info.TaskInfo;
import com.gp.info.TaskRouteInfo;
import com.gp.svc.TaskService;

@Service("taskService")
public class TaskServiceImpl implements TaskService{

	@Override
	public List<TaskInfo> getWorkgrupTasks(ServiceContext<?> svcctx, InfoId<Long> workgroupKey, TaskState state,
			Date fromDate) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaskInfo> getPersonalTasks(ServiceContext<?> svcctx, String account, TaskState state, Date fromDate)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InfoId<Long> newTask(ServiceContext<?> svcctx, TaskInfo task) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remoteTask(ServiceContext<?> svcctx, InfoId<Long> taskKey) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TaskInfo getTask(ServiceContext<?> svcctx, InfoId<Long> workgroupKey, InfoId<Long> taskKey)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaskRouteInfo> getTaskRoutes(ServiceContext<?> svcctx, InfoId<Long> workgroupKey, InfoId<Long> taskKey)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InfoId<Long> forwardTask(ServiceContext<?> svcctx, InfoId<Long> workgroupKey, InfoId<Long> taskKey,
			String nextExecutor) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

}
