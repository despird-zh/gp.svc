package com.gp.info;

public class WorkgroupMemberInfo extends GroupUserInfo{
	
	private static final long serialVersionUID = 1L;
	
	private InfoId<Long> userKey;
	
	private int sourceId;
	
	private String userName;
	
	private String email;
	
	private String userType;

	private String sourceName;
	
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

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}


}
