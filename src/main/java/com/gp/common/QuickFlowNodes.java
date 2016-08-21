package com.gp.common;

public class QuickFlowNodes {

	/** the id of root node */
	public static final Long ROOT_NODE = -1l;
	
	/** the id of end node */
	public static final Long END_NODE = -10l;
	
	/**
	 * Execute mode on flow step 
	 **/
	public static enum ExecMode{
		ANYONE_PASS,
		ALL_PASS,
		VETO_REJECT
	}
	
	/**
	 * The state of process flow
	 **/
	public static enum FlowState{
		START,
		END,
		EXPIRE,
	}
	
	/**
	 * The state of process step
	 **/
	public static enum StepState{
		PENDING,
		COMPLETE,
	}
}
