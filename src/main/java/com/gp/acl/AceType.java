/*******************************************************************************
 * Copyright 2016 Gary Diao - gary.diao@outlook.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
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

	OWNER("owner"), 
	USER("user"),
	GROUP("group"),
	ROLE("role"),
	ANYONE("anyone");
	
	public final String value;
	
	private AceType(String value){
		
		this.value = value;
	}
	
	@Override
	public String toString(){
		
		return value;
	}
	
	public static AceType parse(String val){
		
		if(OWNER.value.equalsIgnoreCase(val))
			return OWNER;
		
		if(USER.value.equalsIgnoreCase(val))
			return USER;
		
		if(GROUP.value.equalsIgnoreCase(val))
			return GROUP;
		
		if(ROLE.value.equalsIgnoreCase(val))
			return ROLE;
		
		if(ANYONE.value.equalsIgnoreCase(val))
			return ANYONE;
		
		return OWNER;
	}
}
