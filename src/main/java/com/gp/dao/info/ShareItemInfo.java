package com.gp.dao.info;

import com.gp.info.TraceableInfo;

public class ShareItemInfo extends TraceableInfo<Long> {
	
	private static final long serialVersionUID = 1L;

	private Long workgroupId;
	
	private Long shareId;
	
	private Long cabinetId;
	
	private Long resourceId;
	
	private String resourceType;
	
	public Long getWorkgroupId() {
		return workgroupId;
	}

	public void setWorkgroupId(Long workgroupId) {
		this.workgroupId = workgroupId;
	}

	public Long getShareId() {
		return shareId;
	}

	public void setShareId(Long shareId) {
		this.shareId = shareId;
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
	
	
}
