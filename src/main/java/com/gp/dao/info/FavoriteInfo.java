package com.gp.dao.info;

import com.gp.info.TraceableInfo;

public class FavoriteInfo extends TraceableInfo<Long> {
	
	private static final long serialVersionUID = 1L;

	private String favoriter;

	private Long resourceId;
	
	private String resourceType;

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public String getFavoriter() {
		return favoriter;
	}

	public void setFavoriter(String favoriter) {
		this.favoriter = favoriter;
	}
	
	
}
