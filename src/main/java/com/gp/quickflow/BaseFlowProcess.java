package com.gp.quickflow;

public abstract class BaseFlowProcess implements FlowProcess {

	private String processName;

	
	public BaseFlowProcess(String processName){
		this.processName = processName;
	}

	public String getProcessName(){
		return processName;
	}
}
