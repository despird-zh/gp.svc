package com.gp.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

public class GuavaCache implements Cache{
	
	public GuavaCache (String name,com.google.common.cache.Cache<Object, Object> cache){
		this.name = name;
		this.cache = cache;
	}
	
	/**
	 * The cache implementation
	 */
	private final com.google.common.cache.Cache<Object, Object> cache;

	/**
	 * The cache name
	 */
	private final String name;

	@Override
	public Object getNativeCache() {
		return this.cache;
	}

	@Override
	public ValueWrapper get(Object key) {
		Object Value = cache.getIfPresent(key);
		return (Value != null ? new SimpleValueWrapper(Value) : null);
	}

	@Override
	public void put(Object key, Object value) {
		cache.put(key, value);
	}

	@Override
	public void evict(Object key) {
		cache.invalidate(key);
	}

	@Override
	public void clear() {
		cache.invalidateAll();
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key, Class<T> type) {
		Object existingValue = cache.getIfPresent(key);
		
		return (T)existingValue;
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		
		Object existingValue = cache.getIfPresent(key);
		if (existingValue == null) {
			cache.put(key, value);
			return null;
		} else {
			return new SimpleValueWrapper(existingValue);
		}

	}
}
