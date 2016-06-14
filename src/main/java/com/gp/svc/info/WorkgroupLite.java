package com.gp.svc.info;

import java.util.Date;

public class WorkgroupLite{

	private static final long serialVersionUID = -5788108460179216377L;

	private String imageFormat;
	
	private String imageExt;
	
	private Date imageTouch;

	private String adminName;
	
	public String getImageFormat() {
		return imageFormat;
	}

	public void setImageFormat(String imageFormat) {
		this.imageFormat = imageFormat;
	}

	public String getImageExt() {
		return imageExt;
	}

	public void setImageExt(String imageExt) {
		this.imageExt = imageExt;
	}

	public Date getImageTouch() {
		return imageTouch;
	}

	public void setImageTouch(Date imageTouch) {
		this.imageTouch = imageTouch;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
}
