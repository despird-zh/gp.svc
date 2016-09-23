package com.gp.svc.info;

import com.gp.dao.info.UserInfo;

public class UserExt extends UserInfo{

	private static final long serialVersionUID = -4298738524459869982L;

	private String sourceName;
	
	private String sourceShortName;
	
	private String sourceAbbr;

	private String storageName;

	public String getSourceShortName() {
		return sourceShortName;
	}

	public void setSourceShortName(String sourceShortName) {
		this.sourceShortName = sourceShortName;
	}

	public String getSourceAbbr() {
		return sourceAbbr;
	}

	public void setSourceAbbr(String sourceAbbr) {
		this.sourceAbbr = sourceAbbr;
	}

	public String getStorageName() {
		return storageName;
	}

	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}	
}
