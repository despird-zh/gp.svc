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
package com.gp.acl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gp.acl.AcePrivilege;
import com.gp.acl.AceType;
import com.gp.common.GeneralConstants;
import com.gp.info.InfoId;

/**
 * EntryAcl is the entry access control list, item of it is access control setting for visitor
 * 
 * @author despird-zh
 * @version 0.1 2014-3-1
 * @since 0.1
 * 
 * @see Ace
 **/
public class Acl {
	
	InfoId<Long> aclId ;
	
	private Map<String,Ace> aclmap = null;
	
	/**
	 * Constructor with acl name 
	 * 
	 * @param aclName the acl name
	 **/
	public Acl(){
		aclmap = new HashMap<String,Ace>();
		Ace owner = new Ace(AceType.OWNER, null, AcePrivilege.DELETE);
		aclmap.put(owner.getMapKey(),owner);
		Ace other = new Ace(AceType.EVERYONE, null, AcePrivilege.READ);
		aclmap.put(other.getMapKey(), other);
	}
	
	/**
	 * Constructor with acl name and ace array
	 * 
	 * @param aclName the acl name
	 * @param aceArray the ace array
	 **/
	public Acl(Ace ... aceArray){
		
		aclmap = new HashMap<String,Ace>();
		
		if(null == aceArray)
			
			return;
		else{
			
			for(Ace ace:aceArray){
				aclmap.put(ace.getMapKey(), ace);
			}
		}
	}

	/**
	 * Add Ace to Acl
	 * 
	 * @param ace the access control entry
	 * @param merge true:merge;false override.
	 **/
	public void addAce(Ace ace,boolean merge){
		
		Ace exist_ace = aclmap.get(ace.getMapKey());
		if(exist_ace != null){

			exist_ace.setPrivilege(ace.getPrivilege(), merge);
				
			exist_ace.setPermissions(ace.getPermissions(), merge);

		}else{
			// none
			this.aclmap.put(ace.getMapKey(),ace);
		}
	}
		
	public InfoId<Long> getAclId() {
		return aclId;
	}

	public void setAclId(InfoId<Long> aclId) {
		this.aclId = aclId;
	}

	/**
	 * Get all ace list
	 **/
	public Collection<Ace> getAllAces(){
		
		return aclmap.values();
	}
	
	/**
	 * Get the user aces
	 * @return the entry ace list 
	 **/
	public List<Ace> getUserAces(){
		List<Ace> uaces = new ArrayList<Ace>();
		
		for(Map.Entry<String, Ace> entry: aclmap.entrySet()){
			
			if(AceType.USER == entry.getValue().getType())
				uaces.add(entry.getValue());
		}
		
		return uaces;
	}
		
	/**
	 * Get the group aces
	 * @return the entry ace list 
	 **/
	public List<Ace> getGroupAces(){
		
		List<Ace> gaces = new ArrayList<Ace>();
		
		for(Map.Entry<String, Ace> entry: aclmap.entrySet()){
			
			if(AceType.GROUP == entry.getValue().getType())
				gaces.add(entry.getValue());
		}
		
		return gaces;
	}

	public Ace getAce(AceType type, String subject){
		
		String mapkey = type.value + GeneralConstants.KEYS_SEPARATOR + subject;		
		return aclmap.get(mapkey);
	}
		
	@Override
	public boolean equals(Object other) {
		
		return hashCode() == other.hashCode();
	}
	
	@Override
	public int hashCode() {
		
		int sumAces = 0;
		if(null != aclmap){
			for(Ace ace:aclmap.values()){
				
				sumAces += ace.hashCode();
			}
		}
		
		return sumAces;
	}

}
