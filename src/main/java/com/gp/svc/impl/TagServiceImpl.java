package com.gp.svc.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.common.IdKey;
import com.gp.common.ServiceContext;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.PseudoDAO;
import com.gp.dao.TagDAO;
import com.gp.dao.TagRelDAO;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.info.TagInfo;
import com.gp.info.TagRelInfo;
import com.gp.svc.CommonService;
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
	CommonService idservice;
	
	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public List<TagInfo> getTags(ServiceContext<?> svcctx, String tagType) throws ServiceException {
		
		List<TagInfo> result;
		try{
			result = tagdao.queryTags(tagType, null, "");
		}catch(DataAccessException dae){
			throw new ServiceException("fail query tags",dae);
		}
		return result;
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public List<TagInfo> getTags(ServiceContext<?> svcctx, String tagType, String category) throws ServiceException {
		List<TagInfo> result;
		try{
			result = tagdao.queryTags(tagType, category, "");
		}catch(DataAccessException dae){
			throw new ServiceException("fail query tags",dae);
		}
		return result;
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public List<TagInfo> getTags(ServiceContext<?> svcctx, String category, InfoId<?> objectId) throws ServiceException {
		
		List<TagInfo> result;
		Map<String, Object> paramap = new HashMap<String, Object>();
		StringBuffer SQL = new StringBuffer();
		
		SQL.append("SELECT rel.rel_id, tag.tag_id, ");
		SQL.append("tag.tag_type, rel.tag_name, ");
		SQL.append("rel.category, tag.tag_color, ");
		SQL.append("rel.modifier, ");
		SQL.append("rel.last_modified ");
		SQL.append("FROM gp_tag_rel rel ");
		SQL.append("LEFT JOIN gp_tags tag on (rel.category = tag.category and rel.tag_name = tag.tag_name) ");
		SQL.append("WHERE rel.resource_type = :type ");
		paramap.put("type", objectId.getIdKey());
		if(StringUtils.isNotBlank(category)){
			SQL.append("AND rel.category = :category ");
			paramap.put("category", category);
		}
		SQL.append("AND resource_id = :objectId");
		paramap.put("objectId", objectId.getId());
		
		try{
			NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("SQL : {} / PARAMS : {}", SQL.toString(), paramap.toString());
			}
			result = jtemplate.query(SQL.toString(), paramap, tagdao.getRowMapper());
			
		}catch(DataAccessException dae){
			throw new ServiceException("fail query tags",dae);
		}
		return result;
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean newTag(ServiceContext<?> svcctx, TagInfo taginfo) throws ServiceException {
		
		if(!InfoId.isValid(taginfo.getInfoId())){
			
			InfoId<Long> id = idservice.generateId(IdKey.TAG, Long.class);
			taginfo.setInfoId(id);
		}
		
		return tagdao.create(taginfo) > 0;

	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR)
	@Override
	public void removeTag(ServiceContext<?> svcctx, String tagType, String tagName) throws ServiceException {
		
		try{
			tagdao.deleteTag(tagType,null, tagName);
		}catch(DataAccessException dae){
			throw new ServiceException("fail delete tags",dae);
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR)
	@Override
	public void removeTag(ServiceContext<?> svcctx, InfoId<Long> tagKey) throws ServiceException {
		
		try{
			tagdao.delete(tagKey);
		}catch(DataAccessException dae){
			throw new ServiceException("fail delete tags",dae);
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR)
	@Override
	public void attachTag(ServiceContext<?> svcctx, InfoId<?> objectId, String category, String tagName)
			throws ServiceException {
		try{
			List<TagInfo> tags = tagdao.queryTags(null, category, tagName);
			if(CollectionUtils.isEmpty(tags)){
				InfoId<Long> tid = idservice.generateId(IdKey.TAG, Long.class);
				TagInfo tag = new TagInfo();
				tag.setInfoId(tid);
				//tag.setTagType(tagType);
				tag.setCategory(category);
				tag.setTagName(tagName);
				svcctx.setTraceInfo(tag);
				
				tagdao.create(tag);
			}
			TagRelInfo rel = new TagRelInfo();
			InfoId<Long> rid = idservice.generateId(IdKey.TAG_REL, Long.class);
			rel.setInfoId(rid);
			rel.setResourceId((long)objectId.getId());
			rel.setResourceType(objectId.getIdKey());
			rel.setTagName(tagName);
			rel.setCategory(category);
			svcctx.setTraceInfo(rel);
			tagreldao.create(rel);
			
		}catch(DataAccessException dae){
			throw new ServiceException("fail delete tags",dae);
		}
	}

}
