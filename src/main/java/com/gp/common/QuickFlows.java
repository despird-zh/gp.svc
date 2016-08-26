package com.gp.common;

public class QuickFlows {

	/** the id of root node */
	public static final Long ROOT_NODE = -1l;
	
	/** the id of end node */
	public static final Long END_NODE = -10l;
	
	/**
	 * Execute mode on flow step 
	 **/
	public enum ExecMode{
		ANYONE_PASS,
		ALL_PASS,
		VETO_REJECT
	}

	/**
	 * The default executor on flow node
	 **/
	public static enum DefaultExecutor{

		WGROUP_MANAGER,
		WGROUP_ADMIN,
		RESOURCE_OWNER,
		FLOW_OWNER,
		FLOW_ATTENDEE;

		public static boolean contains(String checkStr)
		{
			for(DefaultExecutor choice : values())
				if (choice.name().equals(checkStr))
					return true;
			return false;
		}
	}

	/**
	 * The state of process flow
	 **/
	public enum FlowState{
		START,
		END,
		EXPIRE,
	}
	
	/**
	 * The state of process step
	 **/
	public enum StepState{
		PENDING,
		COMPLETE,
	}
}
