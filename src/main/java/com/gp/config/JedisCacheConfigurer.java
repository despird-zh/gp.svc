package com.gp.config;

import java.util.Collection;
import java.util.Collections;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Conditional Configuration, when gp.cache is jedis following config works. 
 **/
@Configuration
@ConditionalOnProperty(prefix = "gp", name = "cache", havingValue="jedis")
@EnableCaching(proxyTargetClass = false)  
@Order(Ordered.HIGHEST_PRECEDENCE + ServiceConfigurer.SERVICE_PRECEDENCE - 10)
public class JedisCacheConfigurer {

	static Logger LOGGER = LoggerFactory.getLogger(GuavaCacheConfigurer.class);
	
    @Resource
    ConfigurableEnvironment environment;

    @Bean
    public PropertiesPropertySource propertySource() {
        return (PropertiesPropertySource) environment.getPropertySources().get("application");
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory(sentinelConfiguration(), poolConfiguration());
    }

    @Bean
    public RedisSentinelConfiguration sentinelConfiguration() {
        return new RedisSentinelConfiguration(propertySource());
    }

    @Bean
    public JedisPoolConfig poolConfiguration() {
    		JedisPoolConfig config = new JedisPoolConfig();
        // add your customized configuration if needed
        return config;
    }

    @Bean
    RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object, Object>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;
    }

    @Bean
    CacheManager cacheManager() {
    		if(LOGGER.isDebugEnabled())
			LOGGER.debug("Building a redis cacha manager");
    		
    		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate());
    		
    		Collection <String> names = Collections.emptySet();
    		names.add(ServiceConfigurer.SYSSETTING_CACHE);
    		names.add(ServiceConfigurer.TRANSFER_CACHE);
    		names.add(ServiceConfigurer.DICTIONARY_CACHE);
    		
    		cacheManager.setCacheNames(names);
    		return cacheManager;
    }
	
}
