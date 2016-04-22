package com.gp.info;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class WorkgroupInfo extends TraceableInfo<Long>{
	
	private static final long serialVersionUID = -8823921041371521351L;

	@NotEmpty
	private String workgroupName;
	
	@NotNull
	private int sourceId;
	
	private String description;
	
	private String hashCode;
	
	@NotEmpty
	private String state;
	
	@NotEmpty
	private String admin;
	
	@Min(0)
	@NotNull
	private Long publishCabinet = 0l;
	
	@Min(0)
	@NotNull
	private Long netdiskCabinet = 0l;

	private Long owm;
	
	@Min(0)
	@NotNull
	private Long orgId;
	
	@Min(0)
	@NotNull
	private Integer storageId;
	
	@NotNull
	private Boolean shareEnable;
	
	@NotNull
	private Boolean linkEnable;
	
	@NotNull
	private Boolean postEnable;
	
	@NotNull
	private Boolean netdiskEnable;
	
	@NotNull
	private Boolean publishEnable;
	
	@NotNull
	private Boolean taskEnable;
	
	private Long avatarId;
	
	public Boolean getShareEnable() {
		return shareEnable;
	}

	public void setShareEnable(Boolean shareEnable) {
		this.shareEnable = shareEnable;
	}

	public Boolean getLinkEnable() {
		return linkEnable;
	}

	public void setLinkEnable(Boolean linkEnable) {
		this.linkEnable = linkEnable;
	}

	public Boolean getPostEnable() {
		return postEnable;
	}

	public void setPostEnable(Boolean postEnable) {
		this.postEnable = postEnable;
	}

	public Boolean getNetdiskEnable() {
		return netdiskEnable;
	}

	public void setNetdiskEnable(Boolean netdiskEnable) {
		this.netdiskEnable = netdiskEnable;
	}

	public Boolean getPublishEnable() {
		return publishEnable;
	}

	public void setPublishEnable(Boolean publishEnable) {
		this.publishEnable = publishEnable;
	}

	public Boolean getTaskEnable() {
		return taskEnable;
	}

	public void setTaskEnable(Boolean taskEnable) {
		this.taskEnable = taskEnable;
	}

	private String creator;
	
	private Date createDate;

	public String getWorkgroupName() {
		return workgroupName;
	}

	public void setWorkgroupName(String workgroupName) {
		this.workgroupName = workgroupName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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

	public String getHashCode() {
		return hashCode;
	}

	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

	public Long getOwm() {
		return owm;
	}

	public void setOwm(Long owm) {
		this.owm = owm;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
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

	public Long getAvatarId() {
		return avatarId;
	}

	public void setAvatarId(Long avatarId) {
		this.avatarId = avatarId;
	}


}
