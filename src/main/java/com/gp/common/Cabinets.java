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

import com.gp.acl.Ace;
import com.gp.acl.AcePrivilege;
import com.gp.acl.AceType;
import com.gp.acl.Acl;
import com.gp.info.InfoId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Define the constant variables related with Cabinet's operation.
 * 
 * @author garydiao
 * @version 0.1 2015-12-2
 **/
public class Cabinets {
	
	static Logger LOGGER = LoggerFactory.getLogger(Cabinets.class);

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
	 * classification of document or folder
	 **/
	public enum Classification{
		TOP_SECRET,
		SECRET,
		CONFIDENTIAL,
		UNCLASSIFIED
	}
	
	/**
	 * InfoId of certain cabinet root 
	 **/
	public static InfoId<Long> ROOT_FOLDER = IdKeys.getInfoId(IdKey.CAB_FOLDER, GeneralConstants.FOLDER_ROOT);
	
	/**
	 * Get the default Cabinet Entry acl setting 
	 **/
	public static Acl getDefaultAcl()
	{
		Acl acl = new Acl();
		Ace everyone = new Ace(AceType.ANYONE, null, AcePrivilege.BROWSE);
		acl.addAce(everyone, true);
		Ace owner = new Ace(AceType.OWNER, null, AcePrivilege.DELETE);
		acl.addAce(owner, true);
		return acl;
	}

}
