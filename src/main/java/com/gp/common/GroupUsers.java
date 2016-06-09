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

public class GroupUsers {
	
	public static Principal ADMIN_USER = new Principal("admin");
	
	public static Principal PESUOD_USER = new Principal("pesudo-user");
	
	static{
		InfoId<Long> pkey = IdKey.USER.getInfoId(0l);
		PESUOD_USER.setUserId(pkey);
		pkey = IdKey.USER.getInfoId(-99l);
		ADMIN_USER.setUserId(pkey);
	}
	
	public static enum UserType{
		LDAP, // LDAP user 
		INLINE, // inline user
		EXTERNAL // external user
	}
	
	public static enum UserState{
		ACTIVE,
		DEACTIVE,
		FROZEN
	}
	
	public static enum GroupType{
		ORG_HIER_MBR, // organization hierarchy group
		WORKGROUP_MBR, // workgroup's member group
		WORKGROUP_GRP, // workgroup's group
		POST_MBR // post attendees
	}
}
