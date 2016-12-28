package com.gp.dao.info;

import com.gp.info.TraceableInfo;

import java.util.Date;

public class TaskRouteInfo extends TraceableInfo<Long> {

	private static final long serialVersionUID = 1L;

	private Long workgroupId;
	
	private Long chronicalTaskId;
	
	private Long taskId;
	
	private Long forwardTaskId;
	
	private String executor;
	
	private String owner;
	
	private String state;
	
	private Date fordwardDate;

	public Long getWorkgroupId() {
		return workgroupId;
	}

	public void setWorkgroupId(Long workgroupId) {
		this.workgroupId = workgroupId;
	}

	public Long getChronicalTaskId() {
		return chronicalTaskId;
	}

	public void setChronicalTaskId(Long chronicalTaskId) {
		this.chronicalTaskId = chronicalTaskId;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getForwardTaskId() {
		return forwardTaskId;
	}

	public void setForwardTaskId(Long forwardTaskId) {
		this.forwardTaskId = forwardTaskId;
	}

	public String getExecutor() {
		return executor;
	}

	public void setExecutor(String executer) {
		this.executor = executer;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getFordwardDate() {
		return fordwardDate;
	}

	public void setFordwardDate(Date fordwardDate) {
		this.fordwardDate = fordwardDate;
	}
	
	
}
