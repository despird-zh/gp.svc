package com.gp.info;

/**
 * This class wrap the summary data of an entry in cabinet.
 * the entry could be a folder or a file
 * 
 * @author garydiao
 * @version 0.1 2015-4-5
 * 
 **/
public class CabSumInfo extends TraceableInfo<Long>{

	private static final long serialVersionUID = -5015443084724070935L;

	private Long cabinetId;
	
	private Long resourceId;
	
	private String resourceType;
	
	private Integer fileSummary;
	
	private Integer folderSummary;
	
	private Long totalSize;

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

	public Integer getFileSummary() {
		return fileSummary;
	}

	public void setFileSummary(Integer fileSummary) {
		this.fileSummary = fileSummary;
	}

	public Integer getFolderSummary() {
		return folderSummary;
	}

	public void setFolderSummary(Integer folderSummary) {
		this.folderSummary = folderSummary;
	}

	public Long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(Long totalSize) {
		this.totalSize = totalSize;
	}
	
	
}
