package com.gp.quickflow;

import java.util.Map;

import com.gp.info.InfoId;

/**
 * The class define the operation bind to Process Flow 
 * @author gary diao
 * @version 0.1 
 **/
public interface StepOperation {

	/**
	 * Get the name of process operation name 
	 **/
	public String getOperation();
	
	/**
	 * Process the reject result scenario 
	 **/
	public void reject(InfoId<Long> stepId, InfoId<?> resourceId, Map<String,Object> procData);
	
	/**
	 * Process the approve result scenario 
	 **/
	public void approve(InfoId<Long> stepId, InfoId<?> resourceId, Map<String,Object> procData);
}
