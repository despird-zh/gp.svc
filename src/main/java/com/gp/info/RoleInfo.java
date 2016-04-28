package com.gp.info;

public class RoleInfo extends TraceableInfo<Integer>{

	private static final long serialVersionUID = 6305292187490625536L;

	private String roleName;

	private String description;
	
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
