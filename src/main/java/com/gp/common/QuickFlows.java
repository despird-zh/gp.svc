package com.gp.common;

public class QuickFlows {

	/** the id of root node */
	public static final Long ROOT_NODE = -1l;
	
	/** the id of end node */
	public static final Long END_NODE = -10l;

	public static final String STEP_PASS = "PASS";
	public static final String STEP_FAIL = "FAIL";

	/**
	 * Execute mode on flow step 
	 **/
	public enum ExecMode{
		ANYONE_PASS,
		ALL_PASS,
		VETO_REJECT,
		CUSTOM,
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
		FAIL,
		EXPIRE,
	}
	
	/**
	 * The state of process step
	 **/
	public enum StepState{
		PENDING,
		COMPLETE,
		IGNORE,
	}
	
	/**
	 * the opinion of approve 
	 **/
	public enum StepOpinion{
		REJECT,
		APPROVE,
		ABSTAIN,
		NONE,
	}
}
