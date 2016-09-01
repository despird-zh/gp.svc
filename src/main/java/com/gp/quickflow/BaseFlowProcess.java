package com.gp.quickflow;

public abstract class BaseFlowProcess implements FlowProcess {

	private String operation;
	
	private boolean procSupport;
	
	private boolean stepSupport;
	
	public BaseFlowProcess(String operation, boolean procSupport, boolean stepSupport){
		this.operation = operation;
		this.procSupport = procSupport;
		this.stepSupport = stepSupport;
	}
	
	public boolean isProcSupport(){
		return this.procSupport;
	}
	
	public boolean isStepSupport(){
		return this.stepSupport;
	}
	
	public String getOperation(){
		return operation;
	}
}
