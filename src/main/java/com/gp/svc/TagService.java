package com.gp.svc;

import java.util.List;

import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.info.TagInfo;

public interface TagService {

	/**
	 * Get the post tag list 
	 **/
	public List<TagInfo> getTags(ServiceContext<?> svcctx, String tagType) throws ServiceException;

	/**
	 * Get the file tag list 
	 **/
	public List<TagInfo> getTags(ServiceContext<?> svcctx, String tagType, String category) throws ServiceException;

	/**
	 * Get the file tag list 
	 **/
	public List<TagInfo> getTags(ServiceContext<?> svcctx, String category, InfoId<?> objectId) throws ServiceException;
	
	/**
	 * create new tag record
	 **/
	public boolean newTag(ServiceContext<?> svcctx, TagInfo taginfo)throws ServiceException;
	
	/**
	 * remove the tag
	 **/
	public void removeTag(ServiceContext<?> svcctx,String tagType, String tag)throws ServiceException;
	
	/**
	 * remove the tag
	 **/
	public void removeTag(ServiceContext<?> svcctx, InfoId<Long> tagKey)throws ServiceException;
	
	
	/**
	 * attach the tag 
	 **/
	public void attachTag(ServiceContext<?> svcctx,InfoId<?> objectId, String tagType, String tag) throws ServiceException;
	
}
