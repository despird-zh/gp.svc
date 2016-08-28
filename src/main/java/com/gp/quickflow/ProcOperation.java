package com.gp.quickflow;

import java.util.Map;

import com.gp.info.InfoId;

public interface ProcOperation {

	/**
	 * Get the name of process operation name 
	 **/
	public String getOperation();
	
	/**
	 * Process the reject result scenario 
	 **/
	public void fail(InfoId<Long> procId, InfoId<?> resourceId, Map<String,Object> procData);
	
	/**
	 * Process the approve result scenario 
	 **/
	public void pass(InfoId<Long> procId, InfoId<?> resourceId, Map<String,Object> procData);
}
