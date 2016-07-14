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

public enum Operations {
	
	/** security operation */
	AUTHENTICATE,
	CHANGE_ACCOUNT_STATE,
	NEW_ACCOUNT,
	UPDATE_ACCOUNT,
	UPDATE_BASIC_SETTING,
	FIND_ACCOUNT,
	FIND_ACCOUNTS,
	REMOVE_ACCOUNT,
	CHANGE_PWD,
	EXIST_ACCOUNT,
	/** workgroup operation */
	NEW_WORKGROUP,
	UPDATE_WORKGROUP,
	FIND_WORKGROUP,
	FIND_WORKGROUPS,
	ADD_WORKGROUP_USER,
	REMOVE_WORKGROUP_USER,
	FIND_WORKGROUP_USERS,
	REMOVE_WORKGROUP,
	CLOSE_WORKGROUP,
	/** instance operation */
	FIND_SOURCE,
	NEW_EXT_SOURCE,
	UPDATE_SOURCE,
	FIND_SOURCES,
	/** system options */
	FIND_SYSOPTIONS,
	UPDATE_SYSOPTION,
	FIND_SYSOPT_GRPS,
	/** storage */
	FIND_STORAGES,
	NEW_STORAGE,
	UPDATE_STORAGE,
	REMOVE_STORAGE,
	FIND_STORAGE,
	/** cabinet */
	FIND_CABINETS,
	FIND_CABINET,
	NEW_CABINET,
	UPDATE_CABINET,
	REMOVE_CABINET,
	/** organization */
	FIND_ORGHIERS,
	FIND_ORGHIER,
	NEW_ORGHIER,
	UPDATE_ORGHIER,
	REMOVE_ORGHIER,
	ADD_ORGHIER_MEMBER,
	REMOVE_ORGHIER_MEMBER,
	FIND_ORGHIER_MEMBER,
	/** group */
	NEW_GROUP,
	UPDATE_GROUP,
	REMOVE_GROUP,
	FIND_GROUP,
	FIND_GROUPS,
	FIND_GROUP_USERS,
	ADD_GROUP_USER,
	REMOVE_GROUP_USER,
	/** folder */
	NEW_FOLDER,
	UPDATE_FOLDER,
	REMOVE_FOLDER,
	FIND_FOLDER,
	FIND_FOLDERS,
	/** file */
	NEW_FILE,
	UPDATE_FILE,
	REMOVE_FILE,
	MOVE_FILE,
	COPY_FILE,
	FIND_FILE,
	FIND_FILES,
	/** image */
	NEW_IMAGE,
	UPDATE_IMAGE,
	REMOVE_IMAGE,
	FIND_IMAGE,
	FIND_IMAGES,
	UPDATE_DICT,
	FIND_DICTS,
	/** binary */
	FETCH_BIN_CHUNK,
	FETCH_BIN,
	STORE_BIN_CHUNK,
	STORE_BIN,
	NEW_BIN,
	REMOVE_BIN,
	/** activity log */
	FIND_ACT_LOGS,
	/** system measure */
	FIND_MEASURE,
	FIND_MEASURES,
	FIND_FAV_SUM,
	FIND_USER_SUM,
	FIND_WORKGROUP_SUM,
	FIND_CAB_SUM,
	/** tag */
	FIND_TAGS,
	ATTACH_TAG,
	DETACH_TAG,
	
}
