package com.gp.quickflow;

public abstract class BaseFlowOperation implements FlowOperation{

	private String operation;
	
	private boolean procSupport;
	
	private boolean stepSupport;
	
	public BaseFlowOperation(String operation, boolean procSupport, boolean stepSupport){
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
