package com.gp.dao;

import java.util.List;

import com.gp.info.TagInfo;

public interface TagDAO extends BaseDAO<TagInfo>{

	/**
	 * Query the tags by condition, if the category not set, it use tag type and tag name 
	 * to filter out the result.
	 * 
	 * @param tagType the tag type
	 * @param category the tag category accept null value
	 * @param tagName the tag name filter condition
	 * @return List<TagInfo> the query result  
	 **/
	public List<TagInfo> queryTags(String tagType, String category, String tagName);
	
	/**
	 * Remove the tag definition
	 * 
	 * @param tagType the type of tag POST,FILE, etc.
	 * @param category the category of tag
	 * @param tagName the name of tag.
	 **/
	public int deleteTag(String tagType, String category, String tagName);
}
