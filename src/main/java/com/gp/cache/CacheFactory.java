package com.gp.cache;

import org.springframework.cache.Cache;

import com.google.common.cache.CacheBuilder;

public class CacheFactory {

	private static Cache cacheinstance;
	
	public static Cache getCache(){
		
		if(cacheinstance == null)
			buildCache();
		
		return cacheinstance;
	}
	
	private static void buildCache(){
		
//		Long maximumSize = null;
//
//		Long expireAfterAccess = null;
//
//		Long expireAfterWrite = null;
//		
		CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
//	    if (maximumSize != null) {
//	      builder.maximumSize(maximumSize);
//	    }
//	    if (expireAfterAccess != null) {
//	      builder.expireAfterAccess(expireAfterAccess, TimeUnit.SECONDS);
//	    }
//	    if (expireAfterWrite != null) {
//	      builder.expireAfterWrite(expireAfterWrite, TimeUnit.SECONDS);
//	    }
	    
	    com.google.common.cache.Cache<Object, Object> guavaCache= builder.build();
	    cacheinstance = new GuavaCache("guava-cache", guavaCache);
	}
}
