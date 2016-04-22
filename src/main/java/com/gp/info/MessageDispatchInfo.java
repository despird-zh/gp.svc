package com.gp.info;

import java.util.Date;

public class MessageDispatchInfo extends TraceableInfo<Long>{
	
	private static final long serialVersionUID = 1L;

	private Long messageId;
	
	private String messageContent;
	
	private String account;
	
	private String globalAccount;
	
	private Boolean touchFlag;
	
	private Date touchTime;

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getGlobalAccount() {
		return globalAccount;
	}

	public void setGlobalAccount(String globalAccount) {
		this.globalAccount = globalAccount;
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
