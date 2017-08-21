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
package com.gp.common;

import com.gp.info.InfoId;

/**
 * Define the constants related to Group and user etc. 
 *  
 **/
public class GroupUsers {
	
	/**
	 * The Administrator principal 
	 **/
	public static Principal ADMIN_USER = new Principal("admin");
	
	/**
	 * The pseudo user definition
	 **/
	public static Principal PSEUDO_USER = new Principal("pesudo-user");
	
	static{
		InfoId<Long> pkey = IdKeys.getInfoId(IdKey.USER, 0l);
		PSEUDO_USER.setUserId(pkey);
		pkey = IdKeys.getInfoId(IdKey.USER, -99l);
		ADMIN_USER.setUserId(pkey);
	}
	
	/**
	 * the user type definition
	 * <ol>
	 * <li>LDAP - user from ldap server</li>
	 * <li>INLINE - user create in system</li>
	 * <li>EXTERNAL - user from external</li>
	 * </ol>
	 **/
	public static enum UserType{
		LDAP, // LDAP user 
		INLINE, // inline user
		EXTERNAL // external user
	}
	
	/**
	 * the user state
	 * <ol>
	 * <li>ACTIVE - the user active</li>
	 * <li>DEACTIVE - the deactive one, user cannot login</li>
	 * <li>FROZEN - the user cannot log in any more, except admin to unlock it</li>
	 * </ol>
	 **/
	public static enum UserState{
		ACTIVE,
		DEACTIVE,
		FROZEN
	}
	
	/**
	 * Group Type define the group category
	 * <ol>
	 * <li>ORG_HIER_MBR - organization hierarchy group</li>
	 * <li>WORKGROUP_MBR - workgroup's member group</li>
	 * <li>WORKGROUP_GRP - workgroup's group</li>
	 * <li>POST_MBR - post attendees</li>
	 * </ol>
	 **/
	public static enum GroupType{
		ORG_HIER_MBR, // organization hierarchy group
		WORKGROUP_MBR, // workgroup's member group
		WORKGROUP_GRP, // workgroup's group
		POST_MBR // post attendees
	}
	
	/**
	 * Work group member role point out the member's privilege scope
	 * <ol>
	 * <li>MANAGER - access to the full feature of work group</li>
	 * <li>CONTRIBUTOR - create new post, task, doc. etc. </li>
	 * <li>CONSUMER - only browse the resources except hidden ones</li>
	 * </ol>
	 **/
	public static enum WorkgroupMemberRole{
		MANAGER,
		CONTRIBUTOR,
		CONSUMER
	}
	
	/**
	 * Organization hierarchy member role
	 * <ol>
	 * <li>MANAGER - org. node manager</li>
	 * <li>MEMBER - org. node member</li>
	 * </ol> 
	 **/
	public static enum OrgHierMemberRole{
		MANAGER,
		MEMBER
	}

	/**
	 * Post member role
	 * <ol>
	 * <li>HOST - post host</li>
	 * <li>ATTENDEE - host attendee</li>
	 * </ol>
	 **/
	public static enum PostMemberRole{
		HOST,
		ATTENDEE
	}
}
