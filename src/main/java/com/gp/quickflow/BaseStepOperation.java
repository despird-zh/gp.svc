package com.gp.quickflow;

public abstract class BaseStepOperation implements StepOperation{

	private String operation;
	public BaseStepOperation(String operation){
		this.operation = operation;
	}
	
	public String getOperation(){
		return this.operation;
	}
}
