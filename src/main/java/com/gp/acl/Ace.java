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

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.gp.common.GeneralConstants;
import com.gp.info.InfoId;

/**
 * Ace is the access control setting for operator, it could be set at 2 levels.
 * <pre>
 * 1 - Person 
 * 2 - Group
 * </pre> 
 * <p>
 * The setting include privilege and Permission for operation.
 * </p>
 * <pre>
 *   type:user
 *   name:demo_account
 *   privilege: WRITE
 *   permissionSet:MOVE,AUDIT,APPROVE
 * </pre>
 * 
 **/
public class Ace implements Comparable<Ace> {

	InfoId<Long> aceId;
	/** the group/user name */
	private String subject;	
	/** the privilege of role*/
	private Set<AcePrivilege> privileges = new HashSet<AcePrivilege>();
	/** the entry type */
	private AceType type;	
	/** the permission set */
	private Set<String> permissions = new HashSet<String>();
	
	/**
	 * Constructor for user ACE item. default privilege is AcePrivilege.WRITE
	 * 
	 * @param type the AceType
	 * @param name the ace subject name, owner name/user name/ group name/ AclConstants.AceType.Other.name()
	 **/
	public Ace(AceType type, String subject){
		
		this.type = type;
		
		if( type == AceType.ANYONE ) {
			setSubject(type.value);
		} else if(type == AceType.OWNER) {
			setSubject(type.value);
			this.privileges.add( AcePrivilege.WRITE );
			this.privileges.add( AcePrivilege.DELETE );
			this.privileges.add( AcePrivilege.EXEC );
		} else {
			setSubject(subject);
			this.privileges.add( AcePrivilege.WRITE );
		}
		this.privileges.add( AcePrivilege.BROWSE );
		this.privileges.add( AcePrivilege.READ );
	}
	
	/**
	 * Constructor for user ACE item. default AceType.User type
	 * 
	 * @param name the ace subject name, owner name/user name/ group name/ AclConstants.AceType.Other.name()
	 * @param privilege the access control privilege
	 *  
	 **/
	public Ace(String subject, AcePrivilege privilege){
		
		this(AceType.USER, subject);
		this.privileges.add( privilege);

	}
	
	/**
	 * Constructor 
	 * 
	 * @param TypeEnum the ace type
	 * @param name the ace subject name, owner name/user name/ group name/ AclConstants.AceType.Other.name()
	 * @param privilege the access control privilege
	 *  
	 **/
	public Ace(AceType type, String subject, AcePrivilege privilege){
		
		this(type, subject);
		this.privileges.add( privilege );
	}
	
	/**
	 * Constructor 
	 * 
	 * @param type the ace type
	 * @param name the ace subject name, owner name/user name/ group name/ AclConstants.AceType.Other.name()
	 * @param privilege the access control privilege
	 * @param permissions the extend business permission
	 **/
	public Ace(AceType type,  String subject, AcePrivilege ...privileges){
		
		this(type, subject);
		for(AcePrivilege privilege: privileges){
			this.privileges.add( privilege );
		}

	}
	
	/**
	 * Constructor default privilege is AcePrivilege.WRITE
	 * 
	 * @param type the ace type
	 * @param name the ace subject name, owner name/user name/ group name/ AclConstants.AceType.Other.name()
	 * @param permissions the extend business permission
	 **/
	public Ace(AceType type,  String subject, String ... perms){
		
		this(type, subject);
		this.privileges.add( AcePrivilege.WRITE );
		if(perms == null || perms.length == 0)
			return;

		for(String permission:permissions){
			
			permissions.add(permission);
		}
	}
		
	public InfoId<Long> getAceId() {
		return aceId;
	}

	public void setAceId(InfoId<Long> aceId) {
		this.aceId = aceId;
	}

	/**
	 * Generate the key for ace map of acl object.
	 **/
	protected String getMapKey(){
		
		return type.value + GeneralConstants.KEYS_SEPARATOR + this.subject;
	}
	
	/**
	 * Get the name of user or group
	 **/
	public String getSubject(){

		return this.subject;
	}
	
	/**
	 * Set the name of user or group
	 **/
	public void setSubject(String subject){
		
		if(type == AceType.OWNER){
			this.subject = GeneralConstants.OWNER_SUBJECT;
		}
		else if(type == AceType.ANYONE){
			this.subject = GeneralConstants.ANYONE_SUBJECT;		
		}
		else if(StringUtils.isBlank(subject)){
			throw new IllegalArgumentException("the ace subject can not be null");
		}else{
			this.subject = subject;
		}
	}
	
	/**
	 * Get the type the acl entry: user or group 
	 **/
	public AceType getType(){
		
		return this.type;
	}
	
	/**
	 * Get the privilege : none, browse, read, write, delete 
	 **/
	public Set<AcePrivilege> getPrivileges(){
		
		return this.privileges;
	}
	
	/**
	 * Set the privilege : none, browse, read, write, delete 
	 **/
	public void setPrivileges( boolean merge, AcePrivilege ... privileges){
		if(merge && ArrayUtils.isNotEmpty(privileges)) {
			for(AcePrivilege privilege: privileges){
				this.privileges.add( privilege );
			}
		}else {
			this.privileges.clear();
			
			if(ArrayUtils.isEmpty(privileges)) return;
			
			for(AcePrivilege privilege: privileges){
				this.privileges.add( privilege );
			}
		}
	}
	
	/**
	 * Set the privilege : none, browse, read, write, delete 
	 **/
	public void setPrivileges( boolean merge, Set<AcePrivilege> privileges){
		if(merge && CollectionUtils.isNotEmpty(privileges)) {
			for(AcePrivilege privilege: privileges){
				this.privileges.add( privilege );
			}
		}else {
			this.privileges.clear();
			
			if(CollectionUtils.isEmpty(privileges)) return;
			
			this.privileges.addAll(privileges);
		}
	}
	
	/**
	 * Set the privilege 
	 **/
	public void grantPrivileges(AcePrivilege ... privileges){
		
		for(AcePrivilege privilege: privileges){
			this.privileges.add( privilege );
		}
	}
	
	/**
	 * Set the privilege 
	 **/
	public void revokePrivileges(AcePrivilege ... privileges){
		
		for(AcePrivilege privilege: privileges){
			this.privileges.remove( privilege );
		}
	}
	
	public boolean checkPrivilege(AcePrivilege privilege){
		
		if(privileges == null )
			return false;
		
		else{

			return this.privileges.contains(privilege);
		}
	}

	public boolean[] checkPrivileges(AcePrivilege ...privileges){
		
		if(privileges == null || privileges.length == 0)
			return new boolean[0];
		
		else{
			
			boolean[] result = new boolean[privileges.length];
			for(int i = 0; i < privileges.length ; i++){
				
				result[i] = this.privileges.contains(privileges[i]);
			}
			
			return result;
		}
	}
	
	/**
	 * Get the permission set 
	 **/
	public Set<String> getPermissions(){
		
		return this.permissions;
	}
	
	/**
	 * Get the permission set 
	 **/
	public void setPermissions(Set<String> perms, boolean merge){
		
		if (merge)
			this.permissions.addAll(perms);
		else{
			this.permissions.clear();
			this.permissions.addAll(perms);
		}
	}
	
	/**
	 * Grant permission setting to current ace.
	 * 
	 * @param permission
	 **/
	public void grantPermission(String ... permission){
		for(String p:permission){
			this.permissions.add(p);
		}
	}
	
	/**
	 * Revoke permission setting from current ace.
	 * 
	 * @param permission
	 **/
	public void revokePermission(String ... permission){
		for(String p:permission){
			this.permissions.remove(p);
		}
	}
	
	public boolean[] checkPermission(String ... perms){
		
		if(perms == null || perms.length == 0)
			return new boolean[0];
		
		else{
			
			boolean[] result = new boolean[perms.length];
			for(int i = 0; i<perms.length ; i++){
				result[i] = permissions.contains(perms[i]);
			}
			return result;
		}
	}
	
	@Override
	public String toString(){
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("subject").append(GeneralConstants.KEYVAL_SEPARATOR)
			.append(this.subject).append(GeneralConstants.KVPAIRS_SEPARATOR);
		
		sbuf.append("type").append(GeneralConstants.KEYVAL_SEPARATOR)
			.append(this.type).append(GeneralConstants.KVPAIRS_SEPARATOR);
		
		sbuf.append("privilege").append(GeneralConstants.KEYVAL_SEPARATOR)
			.append(this.privileges.toString()).append(GeneralConstants.KVPAIRS_SEPARATOR);
		
		if(null != permissions){
			sbuf.append("permissions").append(GeneralConstants.KEYVAL_SEPARATOR);
			for(String perm : permissions){
				
				sbuf.append(perm).append(GeneralConstants.VALUES_SEPARATOR);
			}
		}
		
		return sbuf.toString();
	}
	
	@Override
	public boolean equals(Object other) {
		// step 1
		if (other == this) {
			return true;
		}
		// step 2
		if (!(other instanceof Ace)) {
			return false;
		}
		// step 3
		Ace that = (Ace) other;

		return new EqualsBuilder()
			.append(this.type, that.getType())
			.append(this.subject, that.getSubject())
			.append(this.privileges, that.getPrivileges())
			.append(this.permissions, this.getPermissions()).isEquals();
		
	}

	@Override
	public int hashCode() {
				
		return new HashCodeBuilder(17, 37)
			.append(this.type)
			.append(this.subject)
			.append(this.privileges)
			.append(this.permissions).toHashCode();
			
	}
	
	@Override
	public int compareTo(Ace o) {

	    if(this.type.equals(o.type)){
	    	
	    	return this.subject.compareTo(o.subject);
	    }else {
	    	
	    	return this.type.compareTo(o.type);
	    }
	}
}
