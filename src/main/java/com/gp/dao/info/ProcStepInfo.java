package com.gp.dao.info;

import java.util.Date;
import java.util.Set;

import com.gp.info.TraceableInfo;

/**
 * Created by garydiao on 8/18/16.
 */
public class ProcStepInfo extends TraceableInfo<Long>{

	private static final long serialVersionUID = 1L;

	private Long procId;
	
	private Long nodeId;
	
	private String stepName;
	
	private Long prevStep;

    private Date createTime;

	private Date completeTime;

	private Set<String> executors;

    private String state;

	public Long getProcId() {
		return procId;
	}

	public void setProcId(Long procId) {
		this.procId = procId;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public Long getPrevStep() {
		return prevStep;
	}

	public void setPrevStep(Long prevStep) {
		this.prevStep = prevStep;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Set<String> getExecutors() {
		return executors;
	}

	public void setExecutors(Set<String> executors) {
		this.executors = executors;
	}
}
