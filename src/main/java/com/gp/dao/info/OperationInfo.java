package com.gp.dao.info;

import com.gp.info.TraceableInfo;

import java.util.Date;

public class OperationInfo extends TraceableInfo<Long> {

	private static final long serialVersionUID = 1L;

	private Long workgroupId;
	
	private String subject;
	
	private String subjectExcerpt;
	
	private Long auditId;

	private Date operationTime;

	private String operation;
	
	private String operationExcerpt;
	
	private String object;
	
	private String objectExcerpt;
	
	private String predicate;
	
	private String predicateExcerpt;

	public Long getWorkgroupId() {
		return workgroupId;
	}

	public void setWorkgroupId(Long workgroupId) {
		this.workgroupId = workgroupId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubjectExcerpt() {
		return subjectExcerpt;
	}

	public void setSubjectExcerpt(String subjectExcerpt) {
		this.subjectExcerpt = subjectExcerpt;
	}

	public Long getAuditId() {
		return auditId;
	}

	public void setAuditId(Long auditId) {
		this.auditId = auditId;
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

	public String getOperationExcerpt() {
		return operationExcerpt;
	}

	public void setOperationExcerpt(String operationExcerpt) {
		this.operationExcerpt = operationExcerpt;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getObjectExcerpt() {
		return objectExcerpt;
	}

	public void setObjectExcerpt(String objectExcerpt) {
		this.objectExcerpt = objectExcerpt;
	}

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public String getPredicateExcerpt() {
		return predicateExcerpt;
	}

	public void setPredicateExcerpt(String predicateExcerpt) {
		this.predicateExcerpt = predicateExcerpt;
	}

}
