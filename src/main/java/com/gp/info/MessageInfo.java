package com.gp.info;

public class MessageInfo extends TraceableInfo<Long>{
	
	private static final long serialVersionUID = 1L;

	private Integer sourceId;
	
	private Long workgroupId;
	
	private Long cabinetId;
	
	private String category;
	
	private Long resourceId;
	
	private String resourceType;
	
	private String operation;
	
	private String msgDictKey;
	
	private String msgParams;

	private String sendAccount;
	
	private String sendGlobalAccount;
	
	private Boolean replyEnable;

	public Long getWorkgroupId() {
		return workgroupId;
	}

	public void setWorkgroupId(Long workgroupId) {
		this.workgroupId = workgroupId;
	}

	public Long getCabinetId() {
		return cabinetId;
	}

	public void setCabinetId(Long cabinetId) {
		this.cabinetId = cabinetId;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMsgDictKey() {
		return msgDictKey;
	}

	public void setMsgDictKey(String msgDictKey) {
		this.msgDictKey = msgDictKey;
	}

	public String getMsgParams() {
		return msgParams;
	}

	public void setMsgParams(String msgParams) {
		this.msgParams = msgParams;
	}

	public Boolean getReplyEnable() {
		return replyEnable;
	}

	public void setReplyEnable(Boolean replyEnable) {
		this.replyEnable = replyEnable;
	}

	public String getSendAccount() {
		return sendAccount;
	}

	public void setSendAccount(String sendAccount) {
		this.sendAccount = sendAccount;
	}

	public String getSendGlobalAccount() {
		return sendGlobalAccount;
	}

	public void setSendGlobalAccount(String sendGlobalAccount) {
		this.sendGlobalAccount = sendGlobalAccount;
	}

	public Integer getSourceId() {
		return sourceId;
	}

	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}
		
}
