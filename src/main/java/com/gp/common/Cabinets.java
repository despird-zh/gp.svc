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

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gp.acl.Ace;
import com.gp.acl.AcePrivilege;
import com.gp.acl.AceType;
import com.gp.acl.Acl;
import com.gp.info.InfoId;
/**
 * Define the constant variables related with Cabinet's operation.
 * 
 * @author garydiao
 * @version 0.1 2015-12-2
 **/
public class Cabinets {
	
	static Logger LOGGER = LoggerFactory.getLogger(Cabinets.class);
	
	public static ObjectMapper JSON_MAPPER = new ObjectMapper();

	/**
	 * enums of cabinet type 
	 **/
	public static enum EntryType{
		
		FOLDER,
		FILE
	}
	
	/**
	 * enums of cabinet type 
	 **/
	public static enum CabinetType{
		
		PUBLISH,
		NETDISK
	}
	
	/**
	 * enums of folder state
	 **/
	public static enum FolderState{
		
		READY,
		COPY,
		MOVE,
		DELETE
	}
	
	/**
	 * enums of file state
	 **/
	public static enum FileState{
		
		BLANK,
		READY,
		COPY,
		MOVE,
		DELETE
	}
	
	/**
	 * InfoId of certain cabinet root 
	 **/
	public static InfoId<Long> ROOT_FOLDER = IdKey.CAB_FOLDER.getInfoId(GeneralConstants.FOLDER_ROOT);
	
	/**
	 * Get the default Cabinet Entry acl setting 
	 **/
	public static Acl getDefaultAcl()
	{
		Acl acl = new Acl();
		Ace everyone = new Ace(AceType.EVERYONE, null, AcePrivilege.BROWSE);
		acl.addAce(everyone, true);
		Ace owner = new Ace(AceType.OWNER, null, AcePrivilege.DELETE);
		acl.addAce(owner, true);
		return acl;
	}
	
	/**
	 * Convert a permission set into a Json String 
	 **/
	public static String toPermString(Set<String> perms){
		if(null == perms)
			return "[]";
		try {
			return JSON_MAPPER.writeValueAsString(perms);
		} catch (JsonProcessingException e) {
			LOGGER.error("Fail convert Set<String> perm to String", e);
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * Convert a Json array String into Permission Set 
	 **/
	public static Set<String> toPermSet(String perms){
		if(StringUtils.isBlank(perms))
			return new HashSet<String>();
		
		try {
			return JSON_MAPPER.readValue(perms, new TypeReference<Set<String>>(){});
		} catch ( IOException e) {
			LOGGER.error("Fail convert Set<String> perm to String", e);
		}
		return new HashSet<String>();
	}
	
	public static String toPropertyString(Map<String, Object> propmap){
		if(null == propmap)
			return "{}";
		try {
			return JSON_MAPPER.writeValueAsString(propmap);
		} catch (JsonProcessingException e) {
			LOGGER.error("Fail convert Map<String, Object> propmap to String", e);
		}
		return StringUtils.EMPTY;
	}
	
	public static Map<String, Object> toPropertyMap(String props){
		if(StringUtils.isBlank(props))
			return new HashMap<String,Object>();
		
		try {
			return JSON_MAPPER.readValue(props, new TypeReference<Map<String, Object>>(){});
		} catch ( IOException e) {
			LOGGER.error("Fail convert Json string to Map<String, Object> propmap", e);
		}
		return new HashMap<String,Object>();
	}
}
