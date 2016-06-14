package com.gp.svc.info;

public class WorkgroupExt{
	
	private static final long serialVersionUID = -8823921041371521351L;

	private String entityCode;
	
	private String nodeCode;
	
	private String instanceName;
	
	private String instanceAbbr;
	
	private String instanceShort;
	
	private String adminName;

	private String managerName;
	
	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getInstanceAbbr() {
		return instanceAbbr;
	}

	public void setInstanceAbbr(String instanceAbbr) {
		this.instanceAbbr = instanceAbbr;
	}

	public String getInstanceShort() {
		return instanceShort;
	}

	public void setInstanceShort(String instanceShort) {
		this.instanceShort = instanceShort;
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
