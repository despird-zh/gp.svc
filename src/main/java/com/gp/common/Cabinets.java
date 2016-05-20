package com.gp.common;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
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
	
	static ObjectMapper JSON_MAPPER = new ObjectMapper();
	
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
			LOGGER.error("Fail convert Set<String> perm to String");
			
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * Convert a Json array String into Permission Set 
	 **/
	public static Set<String> toPermSet(String perms){
		
		try {
			return JSON_MAPPER.readValue(perms, HashSet.class);
		} catch ( IOException e) {
			LOGGER.error("Fail convert Set<String> perm to String");
			
		}
		return new HashSet<String>();
	}
}
