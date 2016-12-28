package com.gp.svc.info;

import com.gp.dao.info.WorkgroupInfo;

public class WorkgroupLite extends WorkgroupInfo{

	private static final long serialVersionUID = 1704692604921194219L;

	private String imageFormat;
	
	private String imageLink;

	private String adminName;
	
	public String getImageFormat() {
		return imageFormat;
	}

	public void setImageFormat(String imageFormat) {
		this.imageFormat = imageFormat;
	}
	
	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}
}
