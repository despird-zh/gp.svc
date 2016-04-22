package com.gp.dao;

import java.util.List;

import com.gp.info.InfoId;
import com.gp.info.TagInfo;

public interface TagDAO extends BaseDAO<TagInfo>{

	/**
	 * query the tags by condition
	 * @param tagType the tag type
	 * @param category the tag category
	 * @param tagName the tag name filter condition
	 * @return List<TagInfo> the query result  
	 **/
	public List<TagInfo> queryTags(String tagType, String category, String tagName);
	
	/**
	 * remove the tag 
	 **/
	public int deleteTag(String tagType, String category, String tagName);
}
