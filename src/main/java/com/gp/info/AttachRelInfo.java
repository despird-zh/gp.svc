package com.gp.info;

public class AttachRelInfo extends TraceableInfo<Long>{
	
	private static final long serialVersionUID = 1L;

	private Long workgroupId;
	
	private Long resourceId;
	
	private String resourceType;
	
	private Long attachId;

	private String attachName;

	private String attachType;
	
	public Long getWorkgroupId() {
		return workgroupId;
	}

	public void setWorkgroupId(Long workgroundId) {
		this.workgroupId = workgroundId;
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

	public Long getAttachId() {
		return attachId;
	}

	public void setAttachId(Long attachId) {
		this.attachId = attachId;
	}

	public String getAttachName() {
		return attachName;
	}

	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}

	public String getAttachType() {
		return attachType;
	}

	public void setAttachType(String attachType) {
		this.attachType = attachType;
	}
	
	
}
