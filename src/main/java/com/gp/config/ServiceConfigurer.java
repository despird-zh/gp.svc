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

	@Bean
	@Order(1)
	public SpringContextUtil springContextUtil() {

		SpringContextUtil springContextUtil = new SpringContextUtil();

	    return springContextUtil;
	}

}
