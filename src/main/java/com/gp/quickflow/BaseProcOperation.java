package com.gp.quickflow;

public abstract class BaseProcOperation implements ProcOperation{

	private String operation;
	
	public BaseProcOperation(String operation){
		this.operation = operation;
	}
	
	public String getOperation(){
		return operation;
	}
}
