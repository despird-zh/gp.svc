package com.gp.dao.info;

import com.gp.info.TraceableInfo;

public class VoteInfo extends TraceableInfo<Long> {
	
	private static final long serialVersionUID = 1L;

	private Long workgroupId;
	
	private Long resourceId;
	
	private String resourceType;
	
	private String voter;
	
	private String opinion;

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

	public String getVoter() {
		return voter;
	}

	public void setVoter(String voter) {
		this.voter = voter;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}	
	
	
}
