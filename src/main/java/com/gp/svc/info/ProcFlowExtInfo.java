package com.gp.svc.info;

import com.gp.dao.info.ProcFlowInfo;

public class ProcFlowExtInfo extends ProcFlowInfo{
	
	private static final long serialVersionUID = 1L;

	private String workgroupName;
	
	private String ownerName;
	
	private int duration;

	public String getWorkgroupName() {
		return workgroupName;
	}

	public void setWorkgroupName(String workgroupName) {
		this.workgroupName = workgroupName;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

}
