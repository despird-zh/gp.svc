package com.gp.info;

import java.util.Date;

public class ChatInfo extends TraceableInfo<Long>{

	private static final long serialVersionUID = -868048419741791198L;

	private String chatType;
	
	private String sponsor;
	
	private String topic;
	
	private boolean ephemeral;
	
	private Long memberGroupId;
	
	private Date createTime;

	public String getChatType() {
		return chatType;
	}

	public void setChatType(String chatType) {
		this.chatType = chatType;
	}

	public String getSponsor() {
		return sponsor;
	}

	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Long getMemberGroupId() {
		return memberGroupId;
	}

	public void setMemberGroupId(Long memberGroupId) {
		this.memberGroupId = memberGroupId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public boolean isEphemeral() {
		return ephemeral;
	}

	public void setEphemeral(boolean ephemeral) {
		this.ephemeral = ephemeral;
	}
	
	
}
