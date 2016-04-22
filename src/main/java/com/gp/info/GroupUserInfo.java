package com.gp.info;

public class GroupUserInfo extends TraceableInfo<Long>{
	
	private static final long serialVersionUID = 1L;

	private Long workgroupId;
	
	private Long groupId;
	
	private String account;

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

	public Long getWorkgroupId() {
		return workgroupId;
	}

	public void setWorkgroupId(Long workgroupId) {
		this.workgroupId = workgroupId;
	}

	
}
