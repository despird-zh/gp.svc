package com.gp.dao.info;

public class CabFileInfo extends CabEntryInfo{
	
	public CabFileInfo() {
		super(false);
	}

	private static final long serialVersionUID = 7281506326387853239L;

	private String profile;
	
	private String properties;

	private Long size;

	private boolean commentOn;
	
	private String version;

	private String versionLabel;
	
	private String state;
	
	private Long binaryId;
	
	private String format;

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getVersionLabel() {
		return versionLabel;
	}

	public void setVersionLabel(String versionLabel) {
		this.versionLabel = versionLabel;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public boolean isCommentOn() {
		return commentOn;
	}

	public void setCommentOn(boolean commentOn) {
		this.commentOn = commentOn;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getBinaryId() {
		return binaryId;
	}

	public void setBinaryId(Long binaryId) {
		this.binaryId = binaryId;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

}
