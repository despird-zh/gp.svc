package com.gp.storage;

import java.util.Map;

import com.gp.common.Storages;

/**
 * The setting of certain storage 
 **/
public class StorageSetting {

	private Map<String, Object> setting = null;
	
	public StorageSetting(Map<String, Object> setting){
		
		this.setting = setting;
	}
	
	public StorageSetting(String json){
		
		setting = Storages.parseSetting(json);

	}
	
	public Map<String, Object> asMap(){
		
		return setting;
	}
	
	public String getValue(String key){
		
		return (String)setting.get(key);
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue(String key, Class<T> clazz){
		
		return (T)setting.get(key);
	}
	
	public String toJsonString(){
		return Storages.wrapSetting(setting);
	}
}
