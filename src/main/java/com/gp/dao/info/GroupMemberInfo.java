package com.gp.dao.info;

import java.util.Date;

import com.gp.info.BaseInfo;
import com.gp.info.InfoId;

public class GroupMemberInfo implements BaseInfo<Long>{

	private static final long serialVersionUID = 1L;

	private Long groupId;
	
	private Long manageId;

	private InfoId<Long> relid;
	
	private String groupName;
	
	private String groupType;
	
	private String description;
	
	private String account;
	
	private String role;
	
	private int sourceId;
	
	private String userName;
	
	private String email;
	
	private String userType;

	private String sourceName;
	
	private Long userId;
	
	private String classification;
	
	private Date createTime;
	
	private boolean postVisible;
	
	public void setInfoId(InfoId<Long> relid){
		this.relid = relid;
	}
	
	@Override
	public InfoId<Long> getInfoId() {
		
		return relid;
	}
	
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

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public boolean getPostVisible() {
		return postVisible;
	}

	public void setPostVisible(boolean postVisible) {
		this.postVisible = postVisible;
	}

}
