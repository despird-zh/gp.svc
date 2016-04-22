package com.gp.info;

public class CabAclInfo extends TraceableInfo<Long>{
	
	private static final long serialVersionUID = 1L;

	private String aclHash;

	public String getAclHash() {
		return aclHash;
	}

	public void setAclHash(String aclHash) {
		this.aclHash = aclHash;
	}	
	
}
