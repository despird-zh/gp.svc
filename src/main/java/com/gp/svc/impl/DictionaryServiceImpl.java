package com.gp.svc.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.common.ServiceContext;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.common.DataSourceHolder;
import com.gp.dao.DictionaryDAO;
import com.gp.dao.PseudoDAO;
import com.gp.exception.ServiceException;
import com.gp.dao.info.DictionaryInfo;
import com.gp.info.InfoId;
import com.gp.svc.DictionaryService;

@Service
public class DictionaryServiceImpl implements DictionaryService{

	public static Logger LOGGER = LoggerFactory.getLogger(DictionaryServiceImpl.class);

	@Autowired
	private DictionaryDAO dictionarydao;
	
	@Autowired
	PseudoDAO pseudodao;

	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public List<DictionaryInfo> getDictEntries(ServiceContext svcctx, String dictGroup, String keyFilter) throws ServiceException {
		StringBuffer SQL = new StringBuffer();
		
		Map<String, Object> paramap = new HashMap<String, Object>();
		
		SQL.append("select * from gp_dictionary where 1=1 ");
		
		NamedParameterJdbcTemplate template = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		
		if(StringUtils.isNotBlank(dictGroup)){
			SQL.append(" and dict_group = :group ");
			paramap.put("group", dictGroup);
		}
		
		SQL.append(" and dict_key like :key ");
		paramap.put("key", keyFilter == null ? "%" : keyFilter + "%");
		
		RowMapper<DictionaryInfo> rmapper = DictionaryDAO.DictionaryMapper;
				
		try{
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("SQL : {} / PARAM : {}", SQL.toString(), paramap.toString());
			}
			List<DictionaryInfo> result = template.query(SQL.toString(), paramap ,rmapper);

			return result;
			
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "Dictionary", dictGroup);
		}
	}

	@CachePut(value=DataSourceHolder.DICTIONARY_CACHE, key="#dictinfo.infoId")
	@Transactional(value = DataSourceHolder.TRNS_MGR)
	@Override
	public boolean updateDictEntry(ServiceContext svcctx, DictionaryInfo dictinfo) throws ServiceException {

		try{
			svcctx.setTraceInfo(dictinfo);
			return dictionarydao.update(dictinfo,FilterMode.NONE) > 0;
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "Dictionary", dictinfo);
		}
	}

	@Cacheable(value=DataSourceHolder.DICTIONARY_CACHE, key="#dictId")
	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public DictionaryInfo getDictEntry(ServiceContext svcctx, InfoId<Long> dictId)throws ServiceException 
	{
		try{
			return dictionarydao.query( dictId);
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "Dictionary", dictId);
		}
	}
	
	@Cacheable(value=DataSourceHolder.DICTIONARY_CACHE, key="#dictkey")
	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public DictionaryInfo getDictEntry(ServiceContext svcctx, String dictKey) throws ServiceException {
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("SELECT * FROM gp_dictionary WHERE dict_key = ?");
		
		JdbcTemplate template = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		
		RowMapper<DictionaryInfo> rmapper = DictionaryDAO.DictionaryMapper;
		
		Object[] parms = new Object[]{				
				dictKey
			};
		
		try{
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("SQL : {} / PARAM : {}", SQL.toString(), ArrayUtils.toString(parms));
			}
			DictionaryInfo result = template.queryForObject(SQL.toString(), parms, rmapper);			
			return result;
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "Dictionary", dictKey);
		}
	}

	@Cacheable(value=DataSourceHolder.DICTIONARY_CACHE, key="#dictKey")
	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public DictionaryInfo getDictEntry(String dictKey, boolean property) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("SELECT * FROM gp_dictionary WHERE dict_key = ?");
		if(property){
			SQL.append(" AND dict_group = 'info_prop'");
		}
		JdbcTemplate template = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		RowMapper<DictionaryInfo> rmapper = DictionaryDAO.DictionaryMapper;
		Object[] parms = new Object[]{				
				dictKey
			};
		
		try{
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("SQL : {} / PARAM : {}", SQL.toString(), ArrayUtils.toString(parms));
			}
			List<DictionaryInfo> result = template.query(SQL.toString(), parms, rmapper);			
			return CollectionUtils.isEmpty(result) ? null : result.get(0);
		}catch(DataAccessException dae){
			LOGGER.error("Fail to find dict entry key :" + dictKey, dae);
			return null;
		}
	}

	@Cacheable(value=DataSourceHolder.DICTIONARY_CACHE, key="#dictGroup")
	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public List<DictionaryInfo> getDictGroupEntries(String dictGroup) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("select * from gp_dictionary where dict_group = ?");
		
		JdbcTemplate template = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		RowMapper<DictionaryInfo> rmapper = DictionaryDAO.DictionaryMapper;
		
		Object[] parms = new Object[]{				
				dictGroup
			};
		
		try{
			List<DictionaryInfo> result = template.query(SQL.toString(), parms ,rmapper);
			return result;
		}catch(DataAccessException dae){
			LOGGER.error("Fail to find dict entry group :" + dictGroup, dae);
			return null;
		}
	}

}
