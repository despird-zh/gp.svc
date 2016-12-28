package com.gp.quickflow;

import java.util.Map;

import com.gp.exception.BaseException;
import com.gp.info.InfoId;

public interface FlowProcess {

	/**
	 * Get the name of process operation name 
	 **/
	public String getProcessName();
	
	/**
	 * Check if the resource type is supported by this process 
	 **/
	public boolean supportCheck(String resourceType);

	/**
	 * Process the reject result scenario 
	 **/
	public void processComplete(InfoId<Long> procId,
							 InfoId<?> resourceId,
							 String customStep,
							 Map<String,Object> procData) throws BaseException;

	/**
	 * Process the step complete trigger event
	 * @return String the key of next step
	 **/
	public String customNextStep(InfoId<Long> stepId,
							   InfoId<?> resourceId,
							   Map<String,Object> procData)throws BaseException;
}
