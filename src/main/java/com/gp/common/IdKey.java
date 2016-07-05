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

import java.util.HashSet;
import java.util.Set;

import com.gp.info.Identifier;
import com.gp.info.InfoId;


public enum IdKey implements Identifier{

	WORKGROUP("gp_workgroups","workgroup_id", Long.class),
	WORKGROUP_MIRROR("gp_workgroup_mirror","mirror_id", Long.class),
	CABINET("gp_cabinets","cabinet_id", Long.class),
	ORG_HIER("gp_org_hier","org_id", Long.class),
	INSTANCE("gp_instances","instance_id", Integer.class),
	USER("gp_users","user_id", Long.class),
	GROUP("gp_groups","group_id", Long.class),
	GROUP_USER("gp_group_user","rel_id", Long.class),
	SYS_OPTION("gp_sys_options","sys_opt_id", Integer.class),
	TAG("gp_tags","tag_id", Long.class),
	TAG_REL("gp_tag_rel","rel_id", Long.class),
	SHARE("gp_shares","share_id", Long.class),
	SHARE_ITEM("gp_share_item","share_item_id", Long.class),
	SHARE_TO("gp_share_to","share_to_id", Long.class),
	ATTACHMENT("gp_attachments","attachment_id", Long.class),
	ATTACH_REL("gp_attach_rel","rel", Long.class),
	CAB_FILE("gp_cab_files","file_id", Long.class),
	CAB_ACL("gp_cab_acl","acl_id", Long.class),
	CAB_ACE("gp_cab_ace","ace_id", Long.class),
	CAB_VERSION("gp_cab_versions","version_id", Long.class),
	CAB_FOLDER("gp_cab_folders","folder_id", Long.class),
	
	CAB_COMMENT("gp_cab_comments","comment_id", Long.class),
	STORAGE("gp_storages","storage_id", Integer.class),
	BINARY("gp_binaries","binary_id", Long.class),
	POST("gp_posts","post_id", Long.class),
	POST_COMMENT("gp_post_comments","comment_id", Long.class),
	VOTE("gp_votes","vote_id", Long.class),
	AUDIT("gp_audits","audit_id", Long.class),
	DICTIONARY("gp_dictionary","dict_id", Long.class),
	PROPERTY("gp_properties","prop_id", Long.class),
	IDENTIFIER("gp_identifier","id_key", String.class),
	TASK("gp_tasks","task_id", Long.class),
	TASK_ROUTE("gp_task_route","rel_id", Long.class),
	MESSAGE("gp_messages","message_id", Long.class),
	MESSAGE_DISPATCH("gp_message_dispatch","rel_id", Long.class),
	IMAGE("gp_images","image_id", Long.class),
	MEASURE("gp_measures","measure_id", Long.class),
	PAGE("gp_pages","page_id", Integer.class),
	ROLE("gp_roles","role_id", Integer.class),
	ROLE_PAGE("gp_role_page","rel_id", Integer.class),
	USER_ROLE("gp_user_role","rel_id", Long.class),
	FAVORITE("gp_favorites","favorite_id", Long.class),
	MBR_SETTING("gp_mbr_setting","rel_id", Long.class),
	/** the summary tables */
	CAB_SUM("gp_cab_summary","rel_id", Long.class),
	USER_SUM("gp_user_summary","rel_id", Long.class),
	WORKGROUP_SUM("gp_workgroup_summary","rel_id", Long.class),
	ACT_LOG("gp_activity_log","log_id", Long.class);

	/**
	 * the custom Identifier set 
	 **/
	private static Set<Identifier> IdSet = new HashSet<Identifier>();
	
	private final String schema;
	private final Class<?> clazz;
	private final String idColumn;
	
	private <T> IdKey(String schema,String idColumn, Class<T> clazz){
		this.idColumn = idColumn;
		this.schema = schema;
		this.clazz = clazz;
	}
	
	@Override
	public String getTable() {
		
		return schema;
	}

	@Override
	public String getSchema() {
		
		return this.schema;
	}

	@Override
	public <T> InfoId<T> getInfoId(T sequence) {
		
		if(sequence == null || !this.clazz.equals(sequence.getClass()))
			throw new UnsupportedOperationException("Sequence type is not supported");
		
		return new InfoId<T>(this.getSchema(), this.idColumn, sequence);
	}
	
	/** 
	 * Finds the value of the given enumeration by name, case-insensitive. 
	 * Throws an IllegalArgumentException if no match is found.  
	 **/
	public static Identifier valueOfIgnoreCase(String name) {

	    for (IdKey enumValue : IdKey.values()) {
	        if (enumValue.name().equalsIgnoreCase(name) 
	        		|| enumValue.getSchema().equalsIgnoreCase(name)) {
	            return enumValue;
	        }
	    }
	    for (Identifier enumValue : IdSet) {
	        if (enumValue.getSchema().equalsIgnoreCase(name)) {
	            return enumValue;
	        }
	    }
	    throw new IllegalArgumentException(String.format(
	            "There is no value with name '%s' in Enum IdKey",name
	        ));
	}
	
	/**
	 * Get the custom Identifier set
	 *  
	 **/
	public static Set<Identifier> getIdentifierSet(){
		
		return IdSet;
	}
	
	/**
	 * Add the custom Identifier  
	 **/
	public static void addIdentifier(Identifier idkey){
		
		IdSet.add(idkey);
	}
}
