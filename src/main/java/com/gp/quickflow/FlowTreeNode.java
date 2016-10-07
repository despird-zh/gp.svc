package com.gp.quickflow;

import java.awt.Point;
import java.util.Set;

public class FlowTreeNode {

	Point position ;
	
	private Long nodeId;
	
	private String nodeName;

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

}
