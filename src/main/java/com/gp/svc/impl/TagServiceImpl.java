package com.gp.svc.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.common.IdKey;
import com.gp.common.ServiceContext;
import com.gp.common.Tags;
import com.gp.config.ServiceConfigurator;
import com.gp.dao.PseudoDAO;
import com.gp.dao.TagDAO;
import com.gp.dao.TagRelDAO;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.info.TagInfo;
import com.gp.info.TagRelInfo;
import com.gp.svc.IdService;
import com.gp.svc.TagService;

@Service("tagService")
public class TagServiceImpl implements TagService{

	Logger LOGGER = LoggerFactory.getLogger(TagServiceImpl.class);

	@Autowired
	TagDAO tagdao;

	@Autowired 
	TagRelDAO tagreldao;
	
	@Autowired
	PseudoDAO pseudodao;
	
	@Autowired
	IdService idservice;
	
	@Transactional(value = ServiceConfigurator.TRNS_MGR, readOnly = true)
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

	@Transactional(value = ServiceConfigurator.TRNS_MGR, readOnly = true)
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

	@Transactional(value = ServiceConfigurator.TRNS_MGR, readOnly = true)
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

	@Transactional(value = ServiceConfigurator.TRNS_MGR, readOnly = true)
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

	@Transactional(value = ServiceConfigurator.TRNS_MGR, readOnly = true)
	@Override
	public List<TagInfo> getWorkgroupTags(ServiceContext<?> svcctx) throws ServiceException {
		List<TagInfo> result;
		try{
			result = tagdao.queryTags(Tags.TagType.WORKGROUP.name(), null, "");
		}catch(DataAccessException dae){
			throw new ServiceException("fail query tags",dae);
		}
		return result;
	}

	@Transactional(value = ServiceConfigurator.TRNS_MGR, readOnly = true)
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

	@Transactional(value = ServiceConfigurator.TRNS_MGR, readOnly = true)
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

	@Transactional(value = ServiceConfigurator.TRNS_MGR, readOnly = true)
	@Override
	public List<TagInfo> getWorkgroupTags(ServiceContext<?> svcctx, String category) throws ServiceException {
		List<TagInfo> result;
		try{
			result = tagdao.queryTags(Tags.TagType.WORKGROUP.name(), category, "");
		}catch(DataAccessException dae){
			throw new ServiceException("fail query tags",dae);
		}
		return result;
	}

	@Transactional(value = ServiceConfigurator.TRNS_MGR, readOnly = true)
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

	@Transactional(value = ServiceConfigurator.TRNS_MGR, readOnly = true)
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

	@Transactional(value = ServiceConfigurator.TRNS_MGR)
	@Override
	public boolean newTag(ServiceContext<?> svcctx, TagInfo taginfo) throws ServiceException {
		
		if(!InfoId.isValid(taginfo.getInfoId())){
			
			InfoId<Long> id = idservice.generateId(IdKey.TAG, Long.class);
			taginfo.setInfoId(id);
		}
		
		return tagdao.create(taginfo) > 0;

	}

	@Transactional(value = ServiceConfigurator.TRNS_MGR)
	@Override
	public void removeTag(ServiceContext<?> svcctx, String tagType, String tagName) throws ServiceException {
		
		try{
			tagdao.deleteTag(tagType,null, tagName);
		}catch(DataAccessException dae){
			throw new ServiceException("fail delete tags",dae);
		}
	}

	@Transactional(value = ServiceConfigurator.TRNS_MGR)
	@Override
	public void removeTag(ServiceContext<?> svcctx, InfoId<Long> tagKey) throws ServiceException {
		
		try{
			tagdao.delete(tagKey);
		}catch(DataAccessException dae){
			throw new ServiceException("fail delete tags",dae);
		}
	}

	@Transactional(value = ServiceConfigurator.TRNS_MGR)
	@Override
	public void attachTag(ServiceContext<?> svcctx, InfoId<?> objectId, String tagType, String tagName)
			throws ServiceException {
		try{
			List<TagInfo> tags = tagdao.queryTags(tagType, null, tagName);
			if(CollectionUtils.isEmpty(tags)){
				InfoId<Long> tid = idservice.generateId(IdKey.TAG, Long.class);
				TagInfo tag = new TagInfo();
				tag.setInfoId(tid);
				tag.setTagType(tagType);
				tag.setTagName(tagName);
				svcctx.setTraceInfo(tag);
				
				tagdao.create(tag);
			}
			TagRelInfo rel = new TagRelInfo();
			InfoId<Long> rid = idservice.generateId(IdKey.TAG_REL, Long.class);
			rel.setInfoId(rid);
			rel.setResourceId((long)objectId.getId());
			rel.setResourceType(tagType);
			rel.setTagName(tagName);
			svcctx.setTraceInfo(rel);
			tagreldao.create(rel);
			
		}catch(DataAccessException dae){
			throw new ServiceException("fail delete tags",dae);
		}
	}

}
