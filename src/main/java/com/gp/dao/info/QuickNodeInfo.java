package com.gp.dao.info;

import java.util.Map;
import java.util.Set;

import com.gp.info.InfoId;
import com.gp.info.TraceableInfo;

/**
 * Created by garydiao on 8/18/16.
 */
public class QuickNodeInfo extends TraceableInfo<Long>{

	private static final long serialVersionUID = 1L;

	public QuickNodeInfo(){}

	public QuickNodeInfo(InfoId<Long> nodeId){
		this.setInfoId(nodeId);
	}

	private Long flowId;
	
	private String nodeName;
	
    private String execMode;

    private Set<Long> prevNodes;

	private Map<String, Long> nextNodeMap;

	private Set<String> executors;

	private String customStep;

	public String getCustomStep() {
		return customStep;
	}

	public void setCustomStep(String customStep) {
		this.customStep = customStep;
	}

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

	public Set<String> getExecutors() {
		return executors;
	}

	public Map<String, Long> getNextNodeMap() {
		return nextNodeMap;
	}

	public void setNextNodeMap(Map<String, Long> nextNodeMap) {
		this.nextNodeMap = nextNodeMap;
	}

	public void setExecutors(Set<String> executors) {
		this.executors = executors;
	}

}
