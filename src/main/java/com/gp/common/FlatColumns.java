package com.gp.common;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;

import com.gp.info.FlatColLocator;
import com.gp.info.FlatColumn;

public class FlatColumns {

	public static enum FilterMode{
		INCLUDE,
		EXCLUDE,
		NONE
	}
	
	public static final FlatColumn ACL_ID = new FlatColumn("acl_id");
	public static final FlatColumn FOLDER_ID = new FlatColumn("folder_id");
	public static final FlatColumn FOLDER_PID = new FlatColumn("folder_pid");
	public static final FlatColumn MODIFIER = new FlatColumn("modifier");
	public static final FlatColumn MODIFY_DATE = new FlatColumn("last_modified");
	
	public static final FlatColumn ADMIN = new FlatColumn("admin");
	public static final FlatColumn MANAGER = new FlatColumn("manager");
	
	public static final FlatColumn MBR_GRP_ID = new FlatColumn("mbr_group_id");
	public static final FlatColumn MBR_ROLE = new FlatColumn("role");
	
	public static final FlatColumn DICT_ZH_CN = new FlatColumn("lbl_zh_cn");
	public static final FlatColumn DICT_EN_US = new FlatColumn("lbl_en_us");
	public static final FlatColumn DICT_FR_FR = new FlatColumn("lbl_fr_fr");
	public static final FlatColumn DICT_DE_DE = new FlatColumn("lbl_de_de");
	public static final FlatColumn DICT_RU_RU = new FlatColumn("lbl_ru_ru");

	public static final FlatColumn IMG_NAME = new FlatColumn("image_name");
	
	public static final FlatColumn STATE = new FlatColumn("state");
	public static final FlatColumn TYPE = new FlatColumn("type");
	public static final FlatColumn MOBILE = new FlatColumn("mobile");
	public static final FlatColumn PHONE = new FlatColumn("phone");
	public static final FlatColumn FULL_NAME = new FlatColumn("full_name");
	public static final FlatColumn EMAIL = new FlatColumn("email");
	public static final FlatColumn AVATAR_ID = new FlatColumn("avatar_id");
	public static final FlatColumn SIGNATURE = new FlatColumn("signature");
	public static final FlatColumn LANGUAGE = new FlatColumn("language");
	public static final FlatColumn TIMEZONE = new FlatColumn("timezone");
	public static final FlatColumn PASSWORD = new FlatColumn("password");
	public static final FlatColumn STORAGE_ID = new FlatColumn("storage_id");
	public static final FlatColumn PUBLISH_CAB_ID = new FlatColumn("publish_cab_id");
	public static final FlatColumn NETDISK_CAB_ID = new FlatColumn("netdisk_cab_id");
	
	public static final FlatColumn ACCOUNT = new FlatColumn("account");
	
	public static final FlatColumn CAPACITY = new FlatColumn("capacity");
	public static final FlatColumn POST_VISIBLE = new FlatColumn("post_visible");

	public static final FlatColumn WORKGROUP_ID = new FlatColumn("workgroup_id");
	/**
	 * Convert the Columns to string set for easy check existence.
	 * @param cols the columns source
	 * @return the set of string elements 
	 **/
	public static Set<String> toColumnSet(FlatColLocator ...cols){
		Set<String> rtv = new HashSet<String>();
		if(ArrayUtils.isEmpty(cols))
			return rtv;
		else{
			for(FlatColLocator col: cols){
				rtv.add(col.getColumn());
			}
		}
		return rtv;
	}
}
