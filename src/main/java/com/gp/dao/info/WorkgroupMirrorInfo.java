package com.gp.dao.info;

import com.gp.info.TraceableInfo;

import java.util.Date;

public class WorkgroupMirrorInfo extends TraceableInfo<Long> {
	
	private static final long serialVersionUID = -8823921041371521351L;

	private Long workgroupId;
	
	private int sourceId;
	
	private String state;
	
	private Long owm;
	
	private Date lastSyncDate;

	public Long getWorkgroupId() {
		return workgroupId;
	}

	public void setWorkgroupId(Long workgroupId) {
		this.workgroupId = workgroupId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getOwm() {
		return owm;
	}

	public void setOwm(Long owm) {
		this.owm = owm;
	}

	public Date getLastSyncDate() {
		return lastSyncDate;
	}

	public void setLastSyncDate(Date lastSyncDate) {
		this.lastSyncDate = lastSyncDate;
	}

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}
	
}
