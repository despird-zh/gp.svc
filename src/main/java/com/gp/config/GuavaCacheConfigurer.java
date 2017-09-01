package com.gp.config;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.common.cache.CacheBuilder;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
/**
 * Conditional Configuration, when gp.cache is guava following config works. 
 **/
@Configuration
@EnableCaching(proxyTargetClass = false)  
@ConditionalOnProperty(prefix = "gp", name = "cache", havingValue="guava")
@Order(Ordered.HIGHEST_PRECEDENCE + ServiceConfigurer.SERVICE_PRECEDENCE - 10)
public class GuavaCacheConfigurer {
	
	static Logger LOGGER = LoggerFactory.getLogger(GuavaCacheConfigurer.class);
	/**
	 * Create the cache for service usage 
	 **/
	@Bean

	public CacheManager cacheManager() {
		
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("Building a guava cacha manager");
		
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
		
		GuavaCache cache2 = new GuavaCache(ServiceConfigurer.TRANSFER_CACHE, CacheBuilder.newBuilder()
	             .expireAfterAccess(30, TimeUnit.MINUTES)
	             .build());
		
		return cache2;
	}
	
	/**
	 * create system setting cache bean
	 **/
	@Bean
	public Cache sysSettingCache(){

		GuavaCache cache2 = new GuavaCache(ServiceConfigurer.SYSSETTING_CACHE, CacheBuilder.newBuilder()
	             .expireAfterAccess(30, TimeUnit.MINUTES)
	             .build());
		
		return cache2;
	}
	
	/**
	 * create dictionary cache bean 
	 **/
	@Bean
	public Cache dictionaryCache(){

		GuavaCache cache2 = new GuavaCache(ServiceConfigurer.DICTIONARY_CACHE, CacheBuilder.newBuilder()
	             .expireAfterAccess(30, TimeUnit.MINUTES)
	             .build());
		
		return cache2;
	}
	
//	public class CacheTypeCondition implements Condition {
//		
//		public CacheTypeCondition(){}
//		
//		@Override
//		public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
//			MultiValueMap<String, Object> attrs = metadata.getAllAnnotationAttributes(CacheType.class.getName());
//
//            for(Object value : attrs.get("value")){
//                return StringUtils.equals(cacheType, (String)value);
//            }
//            return false;
//		}
//		
//	}
//	
//	@Retention(RetentionPolicy.RUNTIME)
//	@Target({ElementType.METHOD})
//	@Conditional(CacheTypeCondition.class)
//	public @interface CacheType{
//	    String value();
//	}
	
	
}
