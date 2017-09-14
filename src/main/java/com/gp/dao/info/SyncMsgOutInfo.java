package com.gp.dao.info;

import com.gp.info.TraceableInfo;

public class SyncMsgOutInfo extends TraceableInfo<Long>{

	private static final long serialVersionUID = 141797892144943311L;

	private Long pushId;
	
	private String entityCode;
	
	private String nodeCode;
	
	private String traceCode;
	
	private Long owm;
	
	private String syncCommand;
	
	private String msgData;

	public Long getPushId() {
		return pushId;
	}

	public void setPushId(Long pushId) {
		this.pushId = pushId;
	}

	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	public String getNodeCode() {
		return nodeCode;
	}

	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}

	public String getTraceCode() {
		return traceCode;
	}

	public void setTraceCode(String traceCode) {
		this.traceCode = traceCode;
	}

	public Long getOwm() {
		return owm;
	}

	public void setOwm(Long owm) {
		this.owm = owm;
	}

	public String getSyncCommand() {
		return syncCommand;
	}

	public void setSyncCommand(String syncCommand) {
		this.syncCommand = syncCommand;
	}

	public String getMsgData() {
		return msgData;
	}

	public void setMsgData(String msgData) {
		this.msgData = msgData;
	}
	
	
}
