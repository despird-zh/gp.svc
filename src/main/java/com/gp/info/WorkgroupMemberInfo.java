package com.gp.info;

public class WorkgroupMemberInfo extends GroupUserInfo{
	
	private static final long serialVersionUID = 1L;
	
	private InfoId<Long> userKey;
	
	private int instanceId;
	
	private String userName;
	
	private String email;
	
	private String userType;

	private String instanceName;
	
	public InfoId<Long> getUserId() {
		return userKey;
	}

	public void setUserId(InfoId<Long> userKey) {
		this.userKey = userKey;
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

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public int getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}
	
}
