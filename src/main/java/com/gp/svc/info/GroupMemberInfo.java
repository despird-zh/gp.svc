package com.gp.svc.info;

import com.gp.info.BaseInfo;
import com.gp.info.InfoId;

public class GroupMemberInfo implements BaseInfo<Long>{

	private static final long serialVersionUID = 1L;

	private Long groupId;
	
	private Long manageId;

	private InfoId<Long> relid;
	public void setInfoId(InfoId<Long> relid){
		this.relid = relid;
	}
	@Override
	public InfoId<Long> getInfoId() {
		
		return relid;
	}
	
	private String groupName;
	
	private String groupType;
	
	private String description;
	
	private String account;
	
	private String role;
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public Long getManageId() {
		return manageId;
	}
	public void setManageId(Long manageId) {
		this.manageId = manageId;
	}
	public InfoId<Long> getRelid() {
		return relid;
	}
	public void setRelid(InfoId<Long> relid) {
		this.relid = relid;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
