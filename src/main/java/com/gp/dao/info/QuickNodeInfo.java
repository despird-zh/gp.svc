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

    private Set<Long> prevNodes;

    private Set<Long> nextNodes;

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

	public Set<Long> getPrevNodes() {
		return prevNodes;
	}

	public void setPrevNodes(Set<Long> prevNodes) {
		this.prevNodes = prevNodes;
	}

	public Set<Long> getNextNodes() {
		return nextNodes;
	}

	public void setNextNodes(Set<Long> nextNodes) {
		this.nextNodes = nextNodes;
	}
}
