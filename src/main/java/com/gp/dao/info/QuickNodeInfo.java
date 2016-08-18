package com.gp.dao.info;

import java.util.Set;

import com.gp.info.TraceableInfo;

/**
 * Created by garydiao on 8/18/16.
 */
public class QuickNodeInfo extends TraceableInfo<Long>{

	private static final long serialVersionUID = 1L;

	private Long flowId;
	
	private String nodeName;
	
    private String execMode;

    private Set<Long> prevSteps;

    private Set<Long> nextSteps;

	public Long getFlowId() {
		return flowId;
	}

	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getExecMode() {
		return execMode;
	}

	public void setExecMode(String execMode) {
		this.execMode = execMode;
	}

	public Set<Long> getPrevSteps() {
		return prevSteps;
	}

	public void setPrevSteps(Set<Long> prevSteps) {
		this.prevSteps = prevSteps;
	}

	public Set<Long> getNextSteps() {
		return nextSteps;
	}

	public void setNextSteps(Set<Long> nextSteps) {
		this.nextSteps = nextSteps;
	}
 
}
