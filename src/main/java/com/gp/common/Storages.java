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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Storages {
	
	static ObjectMapper OBJ_MAPPER = new ObjectMapper();
	
	static Logger LOGGER = LoggerFactory.getLogger(Storages.class);
	
	public static final int DEFAULT_STORAGE_ID = 1;
	
	public static enum StorageType{		
		DISK,
		HDFS
	}
	
	/**
	 * this enum list the keys of storage setting for each binary,
	 * which stored in disk
	 **/
	public static enum StoreSetting{	
		StorePath,
		HdfsHost,
		HdfsPort
	}
	
	public static enum StorageState{		
		OPEN,
		CLOSE,
		FULL,
	}

	/**
	 * Parse the setting json into map 
	 * 
	 * @param setting the setting json
	 * @return the map of setting
	 **/
	public static Map<String,Object> parseSetting(String setting){
		
		try {
			Map<String,Object> result =
				OBJ_MAPPER.readValue(setting, new TypeReference<HashMap<String, Object>>(){});
			
			return result;
		} catch (Exception e) {

			LOGGER.error("fail parse the json into map.", e);
		}
		return new HashMap<String, Object>();
	}
	
	/**
	 * Wrap the setting map into json string
	 * 
	 * @param setting the map of setting
	 * @return the json string 
	 **/
	public static String wrapSetting(Map<String,Object> setting){
		
		try {
			String result =
				OBJ_MAPPER.writeValueAsString(setting);
			
			return result;
		} catch (Exception e) {

			LOGGER.error("fail wrap the map into json.", e);
		}
		return "{}";
	}
}
