package com.gp.info;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class WorkgroupUserInfo extends TraceableInfo<Long>{
	
	private static final long serialVersionUID = 1L;

	@NotNull
	@Min(value=1)
	private Long workgroupId;
	
	@NotNull
	@Size(min=1, max=16)
	private String account;
	
	@NotNull
	@Size(min=1, max=16)
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
