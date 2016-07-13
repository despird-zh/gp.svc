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
