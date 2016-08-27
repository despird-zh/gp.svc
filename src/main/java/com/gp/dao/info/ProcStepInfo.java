package com.gp.dao.info;

import java.util.Date;
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
	
    private String opinion;

    private String comment;
    
    private Date createTime;
    
    private Date executeTime;
 
    private String executor;
    
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

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(Date executeTime) {
		this.executeTime = executeTime;
	}

	public String getExecutor() {
		return executor;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
    
    
}
