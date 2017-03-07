package com.gp.common;

import java.util.Date;
import java.util.Map;

public class JwtPayload {

	private String issuer;
	
	private String audience;
	
	private Date expireTime;
	
	private Date notBefore;
	
	private String subject;
	
	private Date issueAt = new Date();
	
	private String jwtId;
	
	private Map<String, String> claims;
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Map<String, String> getClaims() {
		return claims;
	}
	public void setClaims(Map<String, String> claims) {
		this.claims = claims;
	}
	
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	public String getAudience() {
		return audience;
	}
	public void setAudience(String audience) {
		this.audience = audience;
	}

	public Date getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	public Date getNotBefore() {
		return notBefore;
	}
	public void setNotBefore(Date notBefore) {
		this.notBefore = notBefore;
	}
	public Date getIssueAt() {
		return issueAt;
	}
	public void setIssueAt(Date issueAt) {
		this.issueAt = issueAt;
	}
	public String getJwtId() {
		return jwtId;
	}
	public void setJwtId(String jwtId) {
		this.jwtId = jwtId;
	}
}
