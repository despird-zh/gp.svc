package com.gp.info;

public class PostUserInfo extends TraceableInfo<Long>{
	
	private static final long serialVersionUID = 1L;

	private int sourceId;
	
	private Long workgroupId;
	
	private Long postId;
	
	private String attendee;

	private String globalAccount;
	
	public Long getWorkgroupId() {
		return workgroupId;
	}

	public void setWorkgroupId(Long workgroupId) {
		this.workgroupId = workgroupId;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public String getAttendee() {
		return attendee;
	}

	public void setAttendee(String attendee) {
		this.attendee = attendee;
	}

	public String getGlobalAccount() {
		return globalAccount;
	}

	public void setGlobalAccount(String globalAccount) {
		this.globalAccount = globalAccount;
	}

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}
	
	
}
