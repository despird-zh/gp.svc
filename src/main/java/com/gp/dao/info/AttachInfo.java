package com.gp.dao.info;

import com.gp.info.TraceableInfo;

import java.util.Date;

public class AttachInfo extends TraceableInfo<Long> {
	
	private static final long serialVersionUID = 1L;

	private int sourceId;
	
	private Long workgroupId;
	
	private String attachName;
	
	private Long size;
	
	private String owner;
	
	private String state;
	
	private Long binaryId;
	
	private String hashCode;
	
	private Long owm;
	
	private String format;
	
	private String creator;
	
	private Date createDate;

	public Long getWorkgroupId() {
		return workgroupId;
	}

	public void setWorkgroupId(Long workgroupId) {
		this.workgroupId = workgroupId;
	}

	public String getAttachName() {
		return attachName;
	}

	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
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

	public String getHashCode() {
		return hashCode;
	}

	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

	public Long getOwm() {
		return owm;
	}

	public void setOwm(Long owm) {
		this.owm = owm;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
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

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}
		
}
