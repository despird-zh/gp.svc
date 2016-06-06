package com.gp.dao;

import com.gp.info.InfoId;
import com.gp.info.TagRelInfo;

public interface TagRelDAO extends BaseDAO<TagRelInfo>{

	/**
	 * Delete the tag attached on resource
	 * @param resId the id of resource
	 * @param tagName the tag to be detached 
	 **/
	public int delete(InfoId<?> resId, String tagName);
}
