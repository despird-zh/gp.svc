package com.gp.dao.info;

import com.gp.info.TraceableInfo;

import java.util.Date;

public class NotificationDispatchInfo extends TraceableInfo<Long> {
	
	private static final long serialVersionUID = 1L;

	private Long notificationId;

	private String receiver;

	private Boolean touchFlag;
	
	private Date touchTime;
	
	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
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
