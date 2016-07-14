package com.gp.dao.info;

import com.gp.info.TraceableInfo;

import java.util.Date;

public class NotificationInfo extends TraceableInfo<Long> {
	
	private static final long serialVersionUID = 1L;

	private Integer sourceId;
	
	private Long workgroupId;

	private Long resourceId;
	
	private String resourceType;
	
	private String operation;
	
	private String notifDictKey;
	
	private String notifParams;

	private String sender;
	
	private Date sendTime;

	public Long getWorkgroupId() {
		return workgroupId;
	}

	public void setWorkgroupId(Long workgroupId) {
		this.workgroupId = workgroupId;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Integer getSourceId() {
		return sourceId;
	}

	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}

	public String getNotifDictKey() {
		return notifDictKey;
	}

	public void setNotifDictKey(String notifDictKey) {
		this.notifDictKey = notifDictKey;
	}

	public String getNotifParams() {
		return notifParams;
	}

	public void setNotifParams(String notifParams) {
		this.notifParams = notifParams;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
		
}
