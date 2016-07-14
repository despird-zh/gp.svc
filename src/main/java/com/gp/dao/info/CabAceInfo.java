package com.gp.dao.info;

import com.gp.info.TraceableInfo;

public class CabAceInfo extends TraceableInfo<Long> {
	
	private static final long serialVersionUID = 1L;

	private Long aclId;
	
	private String subject;
	
	private String subjectType;
	
	private int privilege;
	
	private String permissions;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public int getPrivilege() {
		return privilege;
	}

	public void setPrivilege(int privilege) {
		this.privilege = privilege;
	}

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

	public Long getAclId() {
		return aclId;
	}

	public void setAclId(Long aclId) {
		this.aclId = aclId;
	}	
	
}
