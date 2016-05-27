package com.gp.info;

import java.util.Date;

public class CabEntryInfo extends TraceableInfo<Long>{

	private static final long serialVersionUID = 8194433271160911303L;
	
	private Boolean isFolder;
	
	private String entryName;
	
	private Long cabinetId;
	
	private int sourceId;
	
	private Long parentId;
		
	private String hashCode;
		
	private Long aclId;

	private String owner;
		
	private Long owm = 0l;
		
	private String description;
	
	private String creator;
	
	private Date createDate;

	
	public CabEntryInfo(boolean isFolder){
		
		this.isFolder = isFolder;
	}
	
	public Boolean isFolder(){
		
		return this.isFolder;
	}
	
	public Long getCabinetId() {
		return cabinetId;
	}

	public void setCabinetId(Long cabinetId) {
		this.cabinetId = cabinetId;
	}

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getHashCode() {
		return hashCode;
	}

	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

	public Long getAclId() {
		return aclId;
	}

	public void setAclId(Long aclId) {
		this.aclId = aclId;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Long getOwm() {
		return owm;
	}

	public void setOwm(Long owm) {
		this.owm = owm;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEntryName() {
		return entryName;
	}

	public void setEntryName(String entryName) {
		this.entryName = entryName;
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
	
}
