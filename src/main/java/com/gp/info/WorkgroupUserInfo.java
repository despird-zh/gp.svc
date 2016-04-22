package com.gp.info;

public class WorkgroupUserInfo extends TraceableInfo<Long>{
	
	private static final long serialVersionUID = 1L;

	private Long workgroupId;
	
	private String account;
	
	private String role;

	private String globalAccount;
	
	private Long owm;
	
	public Long getWorkgroupId() {
		return workgroupId;
	}

	public void setWorkgroupId(Long workgroupId) {
		this.workgroupId = workgroupId;
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

	public String getGlobalAccount() {
		return globalAccount;
	}

	public void setGlobalAccount(String globalAccount) {
		this.globalAccount = globalAccount;
	}

	public Long getOwm() {
		return owm;
	}

	public void setOwm(Long owm) {
		this.owm = owm;
	}
	
	
}
