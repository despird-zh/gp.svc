package com.gp.info;

public class OrgUserInfo extends TraceableInfo<Long>{

	private static final long serialVersionUID = 1L;

	private Long orgId;
	
	private String account;

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	
}
