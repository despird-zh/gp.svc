package com.gp.common;

import com.gp.info.InfoId;

public class Users {
	
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
}
