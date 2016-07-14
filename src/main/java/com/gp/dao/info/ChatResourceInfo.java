package com.gp.dao.info;

import com.gp.info.TraceableInfo;

public class ChatResourceInfo extends TraceableInfo<Long> {

	private static final long serialVersionUID = -1554353372585996979L;

	private Long chatId;
	
	private Long resourceId;
	
	private String resourceType;

	public Long getChatId() {
		return chatId;
	}

	public void setChatId(Long chatId) {
		this.chatId = chatId;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	
	
}
