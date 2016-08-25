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
	
	private String subject;
	
	private String quoteExcerpt;
	
	private String excerpt;

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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getQuoteExcerpt() {
		return quoteExcerpt;
	}

	public void setQuoteExcerpt(String quoteExcerpt) {
		this.quoteExcerpt = quoteExcerpt;
	}

	public String getExcerpt() {
		return excerpt;
	}

	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
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
