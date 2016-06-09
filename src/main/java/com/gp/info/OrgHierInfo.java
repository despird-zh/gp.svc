package com.gp.info;

public class OrgHierInfo extends TraceableInfo<Long>{

	private static final long serialVersionUID = 1L;

	private Long parentOrg;

	private Long memberGroupId;
	
	private String level;
	
	private String admin;
	
	private String email;
	
	private String manager;
	
	private String orgName;
	
	private String description;

	public Long getParentOrg() {
		return parentOrg;
	}

	public void setParentOrg(Long parentOrg) {
		this.parentOrg = parentOrg;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getMemberGroupId() {
		return memberGroupId;
	}

	public void setMemberGroupId(Long memberGroupId) {
		this.memberGroupId = memberGroupId;
	}
	
	
}
