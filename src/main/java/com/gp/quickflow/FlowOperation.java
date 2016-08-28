package com.gp.quickflow;

import java.util.Map;

import com.gp.info.InfoId;

public interface FlowOperation {

	/**
	 * Get the name of process operation name 
	 **/
	public String getOperation();
	
	public boolean isProcSupport();
	
	public boolean isStepSupport();
	
	/**
	 * Process the reject result scenario 
	 **/
	public void fail(InfoId<Long> procId, InfoId<?> resourceId, Map<String,Object> procData);
	
	/**
	 * Process the approve result scenario 
	 **/
	public void pass(InfoId<Long> procId, InfoId<?> resourceId, Map<String,Object> procData);

	/**
	 * Process the reject result scenario 
	 **/
	public void reject(InfoId<Long> stepId, InfoId<?> resourceId, Map<String,Object> procData);
	
	/**
	 * Process the approve result scenario 
	 **/
	public void approve(InfoId<Long> stepId, InfoId<?> resourceId, Map<String,Object> procData);
}
