package com.gp.dao.info;

import com.gp.info.TraceableInfo;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class CabinetInfo extends TraceableInfo<Long> {
	
	private static final long serialVersionUID = 1L;

	@NotNull
	private Long workgroupId;
	
	private int sourceId;
	
	@NotNull
	private String cabinetName;
	
	private String description;
	
	private String cabinetType;
	
	@NotNull
	private boolean versionable;
	
	private Long capacity;
	
	private Long used;
	
	private Integer storageId;
	
	private String creator;
	
	private Date createDate;

	
	public Long getWorkgroupId() {
		return workgroupId;
	}

	public void setWorkgroupId(Long workgroupId) {
		this.workgroupId = workgroupId;
	}

	public String getCabinetName() {
		return cabinetName;
	}

	public void setCabinetName(String cabinetName) {
		this.cabinetName = cabinetName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isVersionable() {
		return versionable;
	}

	public void setVersionable(boolean versionable) {
		this.versionable = versionable;
	}

	public Long getCapacity() {
		return capacity;
	}

	public void setCapacity(Long capacity) {
		this.capacity = capacity;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCabinetType() {
		return cabinetType;
	}

	public void setCabinetType(String cabinetType) {
		this.cabinetType = cabinetType;
	}

	public Integer getStorageId() {
		return storageId;
	}

	public void setStorageId(Integer storageId) {
		this.storageId = storageId;
	}

	public Long getUsed() {
		return used;
	}

	public void setUsed(Long used) {
		this.used = used;
	}

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}
	
	
}
