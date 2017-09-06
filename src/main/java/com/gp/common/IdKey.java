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

import com.gp.info.Identifier;


public enum IdKey implements Identifier{

	GP_WORKGROUPS("workgroup_id", Long.class),
	GP_WORKGROUP_MIRROR("mirror_id", Long.class),
	GP_CABINETS("cabinet_id", Long.class),
	GP_ORG_HIER("org_id", Long.class),
	GP_SOURCES("source_id", Integer.class),
	GP_USERS("user_id", Long.class),
	GP_GROUPS("group_id", Long.class),
	GP_GROUP_USER("rel_id", Long.class),
	GP_SYS_OPTIONS("sys_opt_id", Integer.class),
	GP_TAGS("tag_id", Long.class),
	GP_TAG_REL("rel_id", Long.class),
	GP_SHARES("share_id", Long.class),
	GP_SHARE_ITEM("share_item_id", Long.class),
	GP_SHARE_TO("share_to_id", Long.class),
	GP_ATTACHMENTS("attachment_id", Long.class),
	GP_ATTACH_REL("rel", Long.class),
	GP_CAB_FILES("file_id", Long.class),
	GP_CAB_ACL("acl_id", Long.class),
	GP_CAB_ACE("ace_id", Long.class),
	GP_CAB_VERSIONS("version_id", Long.class),
	GP_CAB_FOLDERS("folder_id", Long.class),
	
	GP_CAB_COMMENTS("comment_id", Long.class),
	GP_STORAGES("storage_id", Integer.class),
	GP_BINARIES("binary_id", Long.class),
	GP_POSTS("post_id", Long.class),
	GP_POST_COMMENTS("comment_id", Long.class),
	GP_VOTES("vote_id", Long.class),
	GP_AUDITS("audit_id", Long.class),
	GP_DICTIONARY("dict_id", Long.class),
	GP_PROPERTIES("prop_id", Long.class),
	GP_IDENTIFIER("id_key", String.class),
	GP_TASKS("task_id", Long.class),
	GP_TASK_ROUTE("rel_id", Long.class),
	GP_CHATS("chat_id", Long.class),
	GP_CHAT_RESC("rel_id", Long.class),
	GP_CHAT_MSGS("message_id", Long.class),
	GP_CHAT_DISPATCH("rel_id", Long.class),
	
	GP_NOTIFICATIONS("notification_id", Long.class),
	GP_NOTIFICATION_DISPATCH("rel_id", Long.class),
	
	GP_IMAGES("image_id", Long.class),
	GP_MEASURES("measure_id", Long.class),
	GP_PAGES("page_id", Integer.class),
	GP_ROLES("role_id", Integer.class),
	GP_ROLE_PAGE("rel_id", Integer.class),
	GP_USER_ROLE("rel_id", Long.class),
	GP_FAVORITES("favorite_id", Long.class),
	GP_MBR_SETTING("rel_id", Long.class),
	/** the summary tables */
	GP_CAB_SUMMARY("rel_id", Long.class),
	GP_USER_SUMMARY("rel_id", Long.class),
	GP_WORKGROUP_SUMMARY("rel_id", Long.class),
	GP_OPERATIONS("oper_id", Long.class),
	/** the quick flow table */
	GP_QUICK_FLOWS("flow_id", Long.class),
	GP_QUICK_NODE("node_id", Long.class),
	GP_PROC_FLOWS("proc_id", Long.class),
	GP_PROC_STEP("step_id", Long.class),
	GP_PROC_TRAIL("trail_id", Long.class),
	
	GP_OWM( "id", Long.class),
	
	GP_TOKENS("token_id", Long.class);
	
	private final Class<?> clazz;
	private final String idColumn;
	
	private <T> IdKey(String idColumn, Class<T> clazz){
		this.idColumn = idColumn;
		this.clazz = clazz;
	}

	@Override
	public String getSchema() {
		
		return name();
	}

	@Override
	public String getIdColumn() {

		return this.idColumn;
	}

	@Override
	public Class<?> getIdClass(){
		return this.clazz;
	}
}
