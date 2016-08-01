package com.gp.config;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.google.common.cache.CacheBuilder;
import com.gp.common.SpringContextUtil;
import org.springframework.core.annotation.Order;

@Configuration
@ComponentScan(basePackages = { 
		"com.gp.svc.impl",
		"com.gp.dao.impl" 
	})
@EnableCaching(proxyTargetClass = false)  
public class ServiceConfigurer {
	
	public static final String TRNS_MGR = "gpTxManager";
	
	public static final String DATA_SRC = "gpDataSource";
	
	public static final String TRANSFER_CACHE = "fileTransferCache";
	
	public static final String SYSSETTING_CACHE = "sysSettingCache";
	
	public static final String DICTIONARY_CACHE = "dictionaryCache";
	
	@Bean
	@Order(1)
	public SpringContextUtil springContextUtil() {

		SpringContextUtil springContextUtil = new SpringContextUtil();

	    return springContextUtil;
	}
	
	/**
	 * Create the cache for service usage 
	 **/
	@Bean
	@Order(2)
	public CacheManager cacheManager() {
		
		SimpleCacheManager scm = new SimpleCacheManager();
		
		scm.setCaches(Arrays.asList(
				fileTransferCache(),
				sysSettingCache(),
				dictionaryCache()
				)
			);
		return scm;
	}
	
	/**
	 * create file transfer cache bean 
	 **/
	@Bean
	public Cache fileTransferCache(){
		
		GuavaCache cache2 = new GuavaCache(TRANSFER_CACHE, CacheBuilder.newBuilder()
	             .expireAfterAccess(30, TimeUnit.MINUTES)
	             .build());
		
		return cache2;
	}
	
	/**
	 * create system setting cache bean
	 **/
	@Bean
	public Cache sysSettingCache(){
		
		GuavaCache cache2 = new GuavaCache(SYSSETTING_CACHE, CacheBuilder.newBuilder()
	             .expireAfterAccess(30, TimeUnit.MINUTES)
	             .build());
		
		return cache2;
	}
	
	/**
	 * create dictionary cache bean 
	 **/
	@Bean
	public Cache dictionaryCache(){
		
		GuavaCache cache2 = new GuavaCache(DICTIONARY_CACHE, CacheBuilder.newBuilder()
	             .expireAfterAccess(30, TimeUnit.MINUTES)
	             .build());
		
		return cache2;
	}
}
