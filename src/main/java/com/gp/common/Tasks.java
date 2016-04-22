package com.gp.common;

public class Tasks {

	public static enum TaskState{
		
		PENDING,
		IN_PROCESS,
		COMPLETE,
		REVOKE
	}
	
	public static enum TaskOpinion{
		
		APPROVE,
		REJECT,
		RESERVE
	}
}
