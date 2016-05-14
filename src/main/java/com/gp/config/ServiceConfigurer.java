package com.gp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.gp.common.SpringContextUtil;

@Configuration
@ComponentScan(basePackages = { 
		"com.gp.svc.impl",
		"com.gp.dao.impl" 
		})
public class ServiceConfigurer {
	
	@Bean public SpringContextUtil springContextUtil() { 
		
	      return new SpringContextUtil();
	}
}
