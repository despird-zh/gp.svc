package com.gp.quickflow;

import java.awt.Point;
import java.util.Set;

public class FlowNode {
	
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

    @Override  
    public int hashCode(){  
        final int prime = 31;  
        nodeId = prime * nodeId;  
        
        return nodeId.intValue();  
    }  
    
    @Override  
    public boolean equals(Object obj){  
        if(this == obj)  
            return true;  
        if(obj == null)  
            return false;  
        if(getClass() != obj.getClass())  
            return false;  
        final FlowNode other = (FlowNode)obj;  
        if(nodeId != other.nodeId){  
            return false;  
        }  
        return true;  
    }  
}
