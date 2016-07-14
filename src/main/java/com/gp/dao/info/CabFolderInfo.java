package com.gp.dao.info;

public class CabFolderInfo extends CabEntryInfo{

	public CabFolderInfo() {
		super(true);
	}

	private static final long serialVersionUID = 1L;

	private String profile;
	
	private String properties;

	private Integer folderCount = 0;
	
	private Integer fileCount = 0;
	
	private String state;
	
	private Long totalSize = 0l;


	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public Integer getFolderCount() {
		return folderCount;
	}

	public void setFolderCount(Integer folderCount) {
		this.folderCount = folderCount;
	}

	public Integer getFileCount() {
		return fileCount;
	}

	public void setFileCount(Integer fileCount) {
		this.fileCount = fileCount;
	}

	public Long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(Long totalSize) {
		this.totalSize = totalSize;
	}
	
}
