package com.gp.info;

import java.util.Date;

public class OperationLogInfo extends TraceableInfo<Long>{

	private static final long serialVersionUID = 1L;

	private Long workgroupId;
	
	private String account;
	
	private String userName;
	
	private Long auditId;

	private Date operationTime;

	private String operation;
	
	private String objectId;
	
	private String objectExcerpt;
	
	private String predicateId;
	
	private String predicateExcerpt;

	public Long getWorkgroupId() {
		return workgroupId;
	}

	public void setWorkgroupId(Long workgroupId) {
		this.workgroupId = workgroupId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Long getAuditId() {
		return auditId;
	}

	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectExcerpt() {
		return objectExcerpt;
	}

	public void setObjectExcerpt(String objectExcerpt) {
		this.objectExcerpt = objectExcerpt;
	}

	public String getPredicateId() {
		return predicateId;
	}

	public void setPredicateId(String predicateId) {
		this.predicateId = predicateId;
	}

	public String getPredicateExcerpt() {
		return predicateExcerpt;
	}

	public void setPredicateExcerpt(String predicateExcerpt) {
		this.predicateExcerpt = predicateExcerpt;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

}
