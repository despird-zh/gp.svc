package com.gp.svc.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.gp.common.IdKey;
import com.gp.common.ServiceContext;
import com.gp.common.Tags;
import com.gp.dao.PseudoDAO;
import com.gp.dao.TagDAO;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.info.TagInfo;
import com.gp.svc.IdService;
import com.gp.svc.TagService;

@Service("tagService")
public class TagServiceImpl implements TagService{

	Logger LOGGER = LoggerFactory.getLogger(TagServiceImpl.class);

	@Autowired
	TagDAO tagdao;

	@Autowired
	PseudoDAO pseudodao;
	
	@Autowired
	IdService idservice;
	
	@Override
	public List<TagInfo> getPostTags(ServiceContext<?> svcctx) throws ServiceException {
		
		List<TagInfo> result;
		try{
			result = tagdao.queryTags(Tags.TagType.POST.name(), null, "");
		}catch(DataAccessException dae){
			throw new ServiceException("fail query tags",dae);
		}
		return result;
	}

	@Override
	public List<TagInfo> getFolderTags(ServiceContext<?> svcctx) throws ServiceException {
		
		List<TagInfo> result;
		try{
			result = tagdao.queryTags(Tags.TagType.FOLDER.name(), null, "");
		}catch(DataAccessException dae){
			throw new ServiceException("fail query tags",dae);
		}
		return result;
	}

	@Override
	public List<TagInfo> getFileTags(ServiceContext<?> svcctx) throws ServiceException {
	
		List<TagInfo> result;
		try{
			result = tagdao.queryTags(Tags.TagType.FILE.name(), null, "");
		}catch(DataAccessException dae){
			throw new ServiceException("fail query tags",dae);
		}
		return result;
	}

	@Override
	public List<TagInfo> getTaskTags(ServiceContext<?> svcctx) throws ServiceException {
		List<TagInfo> result;
		try{
			result = tagdao.queryTags(Tags.TagType.TASK.name(), null, "");
		}catch(DataAccessException dae){
			throw new ServiceException("fail query tags",dae);
		}
		return result;
	}

	@Override
	public List<TagInfo> getCabinetTags(ServiceContext<?> svcctx) throws ServiceException {
		List<TagInfo> result;
		try{
			result = tagdao.queryTags(Tags.TagType.CABINET.name(), null, "");
		}catch(DataAccessException dae){
			throw new ServiceException("fail query tags",dae);
		}
		return result;
	}

	@Override
	public List<TagInfo> getPostTags(ServiceContext<?> svcctx, String category) throws ServiceException {
		List<TagInfo> result;
		try{
			result = tagdao.queryTags(Tags.TagType.POST.name(), category, "");
		}catch(DataAccessException dae){
			throw new ServiceException("fail query tags",dae);
		}
		return result;
	}

	@Override
	public List<TagInfo> getTaskTags(ServiceContext<?> svcctx, String category) throws ServiceException {
		List<TagInfo> result;
		try{
			result = tagdao.queryTags(Tags.TagType.TASK.name(), category, "");
		}catch(DataAccessException dae){
			throw new ServiceException("fail query tags",dae);
		}
		return result;
	}

	@Override
	public List<TagInfo> getCabinetTags(ServiceContext<?> svcctx, String category) throws ServiceException {
		List<TagInfo> result;
		try{
			result = tagdao.queryTags(Tags.TagType.CABINET.name(), category, "");
		}catch(DataAccessException dae){
			throw new ServiceException("fail query tags",dae);
		}
		return result;
	}

	@Override
	public List<TagInfo> getFolderTags(ServiceContext<?> svcctx, String category) throws ServiceException {
		List<TagInfo> result;
		try{
			result = tagdao.queryTags(Tags.TagType.FOLDER.name(), category, "");
		}catch(DataAccessException dae){
			throw new ServiceException("fail query tags",dae);
		}
		return result;
	}

	@Override
	public List<TagInfo> getFileTags(ServiceContext<?> svcctx, String category) throws ServiceException {
		List<TagInfo> result;
		try{
			result = tagdao.queryTags(Tags.TagType.FILE.name(), category, "");
		}catch(DataAccessException dae){
			throw new ServiceException("fail query tags",dae);
		}
		return result;
	}

	@Override
	public boolean newTag(ServiceContext<?> svcctx, TagInfo taginfo) throws ServiceException {
		
		if(!InfoId.isValid(taginfo.getInfoId())){
			
			InfoId<Long> id = idservice.generateId(IdKey.TAG, Long.class);
			taginfo.setInfoId(id);
		}
		
		return tagdao.create(taginfo) > 0;

	}

	@Override
	public void removeTag(ServiceContext<?> svcctx, String tagType, String tagName) throws ServiceException {
		
		try{
			tagdao.deleteTag(tagType,null, tagName);
		}catch(DataAccessException dae){
			throw new ServiceException("fail delete tags",dae);
		}
	}

	@Override
	public void removeTag(ServiceContext<?> svcctx, InfoId<Long> tagKey) throws ServiceException {
		
		try{
			tagdao.delete(tagKey);
		}catch(DataAccessException dae){
			throw new ServiceException("fail delete tags",dae);
		}
	}

}
