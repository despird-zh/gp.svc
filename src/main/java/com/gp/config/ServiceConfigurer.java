package com.gp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.gp.common.SpringContextUtil;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + ServiceConfigurer.SERVICE_PRECEDENCE)
@ComponentScan(basePackages = { 
		"com.gp.svc.impl",
		"com.gp.dao.impl" 
	})
public class ServiceConfigurer {
	
	/**
	 * The configuration loading precedence. 
	 **/
	public static final int SERVICE_PRECEDENCE = 49;
	
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

}
