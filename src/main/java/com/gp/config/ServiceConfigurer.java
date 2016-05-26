package com.gp.config;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.google.common.cache.CacheBuilder;
import com.gp.common.SpringContextUtil;

@Configuration
@ComponentScan(basePackages = { 
		"com.gp.svc.impl",
		"com.gp.dao.impl" 
	})
public class ServiceConfigurer {
	
	public static final String TRNS_MGR = "gpTxManager";
	
	public static final String DATA_SRC = "gpDataSource";
	
	public static String TRANSFER_CACHE = "fileTransferCache";
	
	public static String SYSSETTING_CACHE = "sysSettingCache";
	
	@Bean 
	public SpringContextUtil springContextUtil() { 
		
	    return new SpringContextUtil();
	}
	
	/**
	 * Create the cache for service usage 
	 **/
	@Bean
	public CacheManager cacheManager() {
		
		SimpleCacheManager scm = new SimpleCacheManager();
		
		scm.setCaches(Arrays.asList(fileTransferCache(),sysSettingCache()));
		return scm;
	}
	
	@Bean
	public Cache fileTransferCache(){
		
		GuavaCache cache2 = new GuavaCache(TRANSFER_CACHE, CacheBuilder.newBuilder()
	             .expireAfterAccess(30, TimeUnit.MINUTES)
	             .build());
		
		return cache2;
	}
	
	@Bean
	public Cache sysSettingCache(){
		
		GuavaCache cache2 = new GuavaCache(SYSSETTING_CACHE, CacheBuilder.newBuilder()
	             .expireAfterAccess(30, TimeUnit.MINUTES)
	             .build());
		
		return cache2;
	}
}
