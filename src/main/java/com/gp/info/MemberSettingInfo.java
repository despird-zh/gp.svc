package com.gp.info;

public class MemberSettingInfo extends TraceableInfo<Long>{

	private static final long serialVersionUID = -344322300228306606L;

	private Long manageId;
	
	private String account;
	
	private String groupType;
	
	private Boolean postVisible;

	public Long getManageId() {
		return manageId;
	}

	public void setManageId(Long manageId) {
		this.manageId = manageId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public Boolean getPostVisible() {
		return postVisible;
	}

	public void setPostVisible(Boolean postVisible) {
		this.postVisible = postVisible;
	}
	
	
}
