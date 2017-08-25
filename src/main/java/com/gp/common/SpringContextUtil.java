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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Class to hold the ApplicationContext instance
 * 
 * @author diaogc
 * @version 0.1 2016-12-11
 * 
 **/
public class SpringContextUtil implements ApplicationContextAware {

	Logger LOGGER = LoggerFactory.getLogger(SpringContextUtil.class);
	
	private static ApplicationContext applicationContext;

	/**
	 * Method to set the ApplicationContext
	 **/
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;
	}

	/**
	 * Method to get the ApplicationContext 
	 **/
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	/**
	 * Get the Spring Bean instance 
	 * @param beanname the name of bean
	 * @param clazz the bean class
	 **/
	public static <T> T getSpringBean(String beanname, Class<T> clazz){
		
		if(null == applicationContext) return null;
		
		T bean = applicationContext.getBean(beanname, clazz);
		return  bean;
	}
	
	/**
	 * Get the Spring Bean instance 
	 **/
	public static <T> T getSpringBean(Class<T> clazz){
		
		if(null == applicationContext) return null;
		
		return applicationContext.getBean(clazz);
	}
	
	/**
	 * Bind the target bean with autowire annotated properties 
	 * @param existingBean 
	 **/
	public static void bindAutowire(Object existingBean) {
		
		if(null == applicationContext) return;
		
		applicationContext.getAutowireCapableBeanFactory().autowireBean(existingBean);
	}
}
