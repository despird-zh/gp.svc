package com.gp.info;

import org.hibernate.validator.constraints.NotEmpty;

public class GroupUserInfo extends TraceableInfo<Long>{
	
	private static final long serialVersionUID = 1L;

	private Long groupId;

	@NotEmpty
	private String account;

	@NotEmpty
	private String role;
	
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	
}
