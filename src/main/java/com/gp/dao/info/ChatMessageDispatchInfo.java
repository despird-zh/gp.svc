package com.gp.dao.info;

import com.gp.info.TraceableInfo;

import java.util.Date;

public class ChatMessageDispatchInfo extends TraceableInfo<Long> {
	
	private static final long serialVersionUID = 1L;

	private Long messageId;

	private String receiver;

	private Boolean touchFlag;
	
	private Date touchTime;

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public Boolean getTouchFlag() {
		return touchFlag;
	}

	public void setTouchFlag(Boolean touchFlag) {
		this.touchFlag = touchFlag;
	}

	public Date getTouchTime() {
		return touchTime;
	}

	public void setTouchTime(Date touchTime) {
		this.touchTime = touchTime;
	}
	
	
}
