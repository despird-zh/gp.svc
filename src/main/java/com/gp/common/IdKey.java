package com.gp.common;

import java.util.HashSet;
import java.util.Set;

import com.gp.info.Identifier;
import com.gp.info.InfoId;


public enum IdKey implements Identifier{

	WORKGROUP("gp_workgroups", Long.class),
	WORKGROUP_MIRROR("gp_workgroup_mirror", Long.class),
	WORKGROUP_USER("gp_workgroup_user", Long.class),
	CABINET("gp_cabinets", Long.class),
	ORG_HIER("gp_org_hier", Long.class),
	ORG_USER("gp_org_user", Long.class),
	INSTANCE("gp_instances", Integer.class),
	USER("gp_users", Long.class),
	GROUP("gp_groups", Long.class),
	GROUP_USER("gp_group_user", Long.class),
	SYS_OPTION("gp_sys_options", Integer.class),
	TAG("gp_tags", Long.class),
	TAG_REL("gp_tag_rel", Long.class),
	SHARE("gp_shares", Long.class),
	SHARE_ITEM("gp_share_item", Long.class),
	SHARE_TO("gp_share_to", Long.class),
	ATTACHMENT("gp_attachments", Long.class),
	ATTACH_REL("gp_attach_rel", Long.class),
	CAB_FILE("gp_cab_files", Long.class),
	CAB_ACL("gp_cab_acl", Long.class),
	CAB_ACE("gp_cab_ace", Long.class),
	CAB_VERSION("gp_cab_versions", Long.class),
	CAB_FOLDER("gp_cab_folders", Long.class),
	CAB_SUMMARY("gp_cab_summary", Long.class),
	CAB_COMMENT("gp_cab_comments", Long.class),
	STORAGE("gp_storages", Integer.class),
	BINARY("gp_binaries", Long.class),
	POST("gp_posts", Long.class),
	POST_COMMENT("gp_post_comments", Long.class),
	POST_USER("gp_post_user", Long.class),
	VOTE("gp_votes", Long.class),
	AUDIT("gp_audits", Long.class),
	DICTIONARY("gp_dictionary", Long.class),
	PROPERTY("gp_properties", Long.class),
	IDENTIFIER("gp_identifier", String.class),
	TASK("gp_tasks", Long.class),
	TASK_ROUTE("gp_task_route", Long.class),
	MESSAGE("gp_messages", Long.class),
	MESSAGE_DISPATCH("gp_message_dispatch", Long.class),
	IMAGE("gp_images", Long.class);

	private static Set<Identifier> IdSet = new HashSet<Identifier>();
	
	private final String schema;
	private final Class<?> clazz;
		
	private <T> IdKey(String schema, Class<T> clazz){
		
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
		
		return new InfoId<T>(this, sequence);
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
	
	public static Set<Identifier> getIdentifierSet(){
		
		return IdSet;
	}
	
	public static void addIdentifier(Identifier idkey){
		
		IdSet.add(idkey);
	}
}
