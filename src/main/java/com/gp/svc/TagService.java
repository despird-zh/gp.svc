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
	public List<TagInfo> getPostTags(ServiceContext<?> svcctx) throws ServiceException;

	/**
	 * Get the folder tag list 
	 **/
	public List<TagInfo> getFolderTags(ServiceContext<?> svcctx) throws ServiceException;

	/**
	 * Get the file tag list 
	 **/
	public List<TagInfo> getFileTags(ServiceContext<?> svcctx) throws ServiceException;

	/**
	 * Get the task tag list 
	 **/
	public List<TagInfo> getTaskTags(ServiceContext<?> svcctx) throws ServiceException;

	/**
	 * Get the cabinet tag list 
	 **/
	public List<TagInfo> getCabinetTags(ServiceContext<?> svcctx) throws ServiceException;

	/**
	 * Get the post tag list 
	 **/
	public List<TagInfo> getPostTags(ServiceContext<?> svcctx, String category) throws ServiceException;

	/**
	 * Get the task tag list 
	 **/
	public List<TagInfo> getTaskTags(ServiceContext<?> svcctx, String category) throws ServiceException;

	/**
	 * Get the cabinet tag list 
	 **/
	public List<TagInfo> getCabinetTags(ServiceContext<?> svcctx, String category) throws ServiceException;

	/**
	 * Get the folder tag list 
	 **/
	public List<TagInfo> getFolderTags(ServiceContext<?> svcctx, String category) throws ServiceException;

	/**
	 * Get the file tag list 
	 **/
	public List<TagInfo> getFileTags(ServiceContext<?> svcctx, String category) throws ServiceException;

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
}
