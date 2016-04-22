package com.gp.acl;

/**
 * The AclType declare the different subject type
 * OWNER - the owner of resource object.
 * USER - the specified user in the system.
 * GROUP - certain the group include multiple users
 * EVERYONE - all the accounts defined in system, include the external and local users
 * 			  even anonymous access subject.
 **/
public enum AceType {

	OWNER("o"), 
	USER("u"),
	GROUP("g"),
	EVERYONE("e");
	
	public final String value;
	
	private AceType(String value){
		
		this.value = value;
	}
	
	@Override
	public String toString(){
		
		return value;
	}
	
	public static AceType parse(String val){
		
		if(OWNER.value.equals(val))
			return OWNER;
		
		if(USER.value.equals(val))
			return USER;
		
		if(GROUP.value.equals(val))
			return GROUP;
		
		if(EVERYONE.value.equals(val))
			return EVERYONE;
		
		return OWNER;
	}
}
