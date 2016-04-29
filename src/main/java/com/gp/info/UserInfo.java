package com.gp.info;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

public class UserInfo extends TraceableInfo<Long>{

	private static final long serialVersionUID = 1L;

	@NotNull
	@Size(min = 1, max = 16)
	private String account;
	
	private String globalAccount;
	
	private int sourceId;
	
	@NotNull
	@Size(min = 1, max = 32)
	private String type;
	
	@NotNull
	private String mobile;
	
	private String phone;

	@NotNull
	@Size(min = 1, max = 24)
	private String fullName;
	
	@NotNull
	@Email
	private String email;
	
	@NotNull
	@Size(min = 1, max = 64)
	private String password;
	
	@NotNull
	@Size(min = 1, max = 16)
	private String state;
	
	private String extraInfo;
	
	private Date createDate;
	
	private Date lastLogonDate;

	private Integer retryTimes;

	private Integer storageId;
	
	@NotNull
	private String language;
	
	@NotNull
	private String timeZone;
	
	private Long publishCabinet;
	
	private Long netdiskCabinet;
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastLogonDate() {
		return lastLogonDate;
	}

	public void setLastLogonDate(Date lastLogonDate) {
		this.lastLogonDate = lastLogonDate;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public Integer getRetryTimes() {
		return retryTimes;
	}

	public void setRetryTimes(Integer retryTimes) {
		this.retryTimes = retryTimes;
	}

	public String getGlobalAccount() {
		return globalAccount;
	}

	public void setGlobalAccount(String glocalAccount) {
		this.globalAccount = glocalAccount;
	}

	public Long getPublishCabinet() {
		return publishCabinet;
	}

	public void setPublishCabinet(Long publishCabinet) {
		this.publishCabinet = publishCabinet;
	}

	public Long getNetdiskCabinet() {
		return netdiskCabinet;
	}

	public void setNetdiskCabinet(Long netdiskCabinet) {
		this.netdiskCabinet = netdiskCabinet;
	}

	public Integer getStorageId() {
		return storageId;
	}

	public void setStorageId(Integer storageId) {
		this.storageId = storageId;
	}

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}
	
	
}
