package com.gp.svc.info;

import com.gp.dao.info.ProcTrailInfo;

public class ProcTrailExtInfo extends ProcTrailInfo{

	private static final long serialVersionUID = 1L;

	private String sourceName;
	
	private String executorName;
	
	private String workgroupName;

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getExecutorName() {
		return executorName;
	}

	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}

	public String getWorkgroupName() {
		return workgroupName;
	}

	public void setWorkgroupName(String workgroupName) {
		this.workgroupName = workgroupName;
	}

}
