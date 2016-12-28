package com.gp.svc;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.dao.info.TagInfo;

public interface TagService {

	/**
	 * Get the post tag list 
	 **/
	public List<TagInfo> getTags(ServiceContext svcctx, String tagType) throws ServiceException;

	/**
	 * Get the tag list by tag type and category
	 **/
	public List<TagInfo> getTags(ServiceContext svcctx, String tagType, String category) throws ServiceException;

	/**
	 * Get the file tag list by category and InfoId
	 **/
	public List<TagInfo> getTags(ServiceContext svcctx, String category, InfoId<Long> objectId) throws ServiceException;
	
	/**
	 * Get the tags of objects
	 * @param objectIds the array of objectId i.e. the resource_id in tag_rel
	 * @return the map of tag set.
	 *  
	 **/
	public Map<InfoId<Long>, Set<TagInfo>> getTags(ServiceContext svcctx, List<InfoId<Long>> objectIds) throws ServiceException;
	
	/**
	 * create new tag record
	 **/
	public boolean newTag(ServiceContext svcctx, TagInfo taginfo)throws ServiceException;
	
	/**
	 * remove the tag
	 **/
	public boolean removeTag(ServiceContext svcctx,String tagType, String tag)throws ServiceException;
	
	/**
	 * remove the tag
	 **/
	public boolean removeTag(ServiceContext svcctx, InfoId<Long> tagKey)throws ServiceException;
	
	
	/**
	 * attach the tag, Need to clarify the category, if tag not exist in master, append a new tag
	 * 
	 **/
	public boolean attachTag(ServiceContext svcctx,InfoId<?> objectId, String category, String tag) throws ServiceException;
	
	/**
	 * detach the tag 
	 **/
	public boolean detachTag(ServiceContext svcctx,InfoId<?> objectId, String tag) throws ServiceException;
	
}
