package com.gp.common;

public class Workgroups {
	
	public static enum State{
		
		READ_WRITE,
		READ_ONLY,
		CLOSE
	}
	
	public static enum Role{
		
		ADMIN,
		MANAGE,
		NORMAL,
		EXTERN
	}	
	
}
