package com.gp.dao.info;

import java.util.Date;
import java.util.Map;

import com.gp.info.TraceableInfo;

/**
 * Created by garydiao on 8/18/16.
 */
public class ProcFlowInfo extends TraceableInfo<Long>{

	private static final long serialVersionUID = 1L;

	private Long workgroupId;

	private Long flowId;
	
	private String description;

    private String procName;

    private Date launchTime;
    
    private Date expireTime;
    
    private String owner;
    
    private String state;
    
	private long resourceId;
	
	private String resourceType;
	
    private Map<String, Object> data;

	private String customProcess;

	private Date completeTime;

	public String getCustomProcess() {
		return customProcess;
	}

	public void setCustomProcess(String customProcess) {
		this.customProcess = customProcess;
	}

	public Long getFlowId() {
		return flowId;
	}

	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProcName() {
		return procName;
	}

	public void setProcName(String procName) {
		this.procName = procName;
	}

	public Date getLaunchTime() {
		return launchTime;
	}

	public void setLaunchTime(Date launchTime) {
		this.launchTime = launchTime;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public long getResourceId() {
		return resourceId;
	}

	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Date getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

	public Long getWorkgroupId() {
		return workgroupId;
	}

	public void setWorkgroupId(Long workgroupId) {
		this.workgroupId = workgroupId;
	}

}
