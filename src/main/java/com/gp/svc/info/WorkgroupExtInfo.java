package com.gp.svc.info;

import com.gp.dao.info.WorkgroupInfo;

public class WorkgroupExtInfo extends WorkgroupInfo{
	
	private static final long serialVersionUID = -7455714273637511174L;

	private String entityCode;
	
	private String nodeCode;
	
	private String sourceName;
	
	private String sourceAbbr;
	
	private String sourceShort;
	
	private String adminName;

	private String managerName;
	
	

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getSourceAbbr() {
		return sourceAbbr;
	}

	public void setSourceAbbr(String sourceAbbr) {
		this.sourceAbbr = sourceAbbr;
	}

	public String getSourceShort() {
		return sourceShort;
	}

	public void setSourceShort(String sourceShort) {
		this.sourceShort = sourceShort;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	public String getNodeCode() {
		return nodeCode;
	}

	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

}
