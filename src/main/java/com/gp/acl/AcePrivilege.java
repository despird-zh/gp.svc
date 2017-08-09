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

public enum AcePrivilege {
	
	BROWSE("browse"),
	READ("read"),
	WRITE("write"),
	DELETE("delete"),
	EXEC("exec");
	
	public final String value;
	
	private AcePrivilege(String value){
		
		this.value = value;
	}
	
	public String toString(String privilege){
		return this.value;
	}
	
	public static AcePrivilege parse(String val){
		
		if(BROWSE.value.equalsIgnoreCase(val))
			return BROWSE;
		
		if(READ.value.equalsIgnoreCase(val))
			return READ;
		
		if(WRITE.value.equalsIgnoreCase(val))
			return WRITE;
		
		if(DELETE.value.equalsIgnoreCase(val))
			return DELETE;
		
		return EXEC;
	}
}
