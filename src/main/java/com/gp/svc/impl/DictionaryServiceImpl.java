package com.gp.svc.impl;

import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.common.ServiceContext;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.DictionaryDAO;
import com.gp.dao.PseudoDAO;
import com.gp.exception.ServiceException;
import com.gp.info.DictionaryInfo;
import com.gp.info.InfoId;
import com.gp.svc.DictionaryService;

@Service("dictionaryService")
public class DictionaryServiceImpl implements DictionaryService{

	public static Logger LOGGER = LoggerFactory.getLogger(DictionaryServiceImpl.class);

	@Autowired
	private DictionaryDAO dictionarydao;
	
	@Autowired
	PseudoDAO pseudodao;
	
	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public List<DictionaryInfo> getDictEntries(ServiceContext svcctx) throws ServiceException {

		StringBuffer SQL = new StringBuffer();
		
		SQL.append("SELECT * FROM gp_dictionary");
		
		JdbcTemplate template = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		
		RowMapper<DictionaryInfo> rmapper = DictionaryDAO.DictionaryMapper;
		
		try{
			List<DictionaryInfo> result = template.query(SQL.toString(), rmapper);
			return result;
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query", dae, "Dictionary");
		}		
	}

	@Cacheable(value=ServiceConfigurer.DICTIONARY_CACHE, key="#dictGroup")
	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public List<DictionaryInfo> getDictEntries(ServiceContext svcctx, String dictGroup) throws ServiceException {
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
			throw new ServiceException("excp.query.with", dae, "Dictionary", dictGroup);
		}
	}

	@CachePut(value=ServiceConfigurer.DICTIONARY_CACHE, key="#dictinfo.infoId")
	@Transactional(value = ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean updateDictEntry(ServiceContext svcctx, DictionaryInfo dictinfo) throws ServiceException {

		try{
			svcctx.setTraceInfo(dictinfo);
			return dictionarydao.update(dictinfo,FilterMode.NONE) > 0;
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "Dictionary", dictinfo);
		}
	}

	@Cacheable(value=ServiceConfigurer.DICTIONARY_CACHE, key="#dictId")
	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public DictionaryInfo getDictEntry(ServiceContext svcctx, InfoId<Long> dictId)throws ServiceException 
	{
		try{
			return dictionarydao.query( dictId);
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "Dictionary", dictId);
		}
	}
	
	@Cacheable(value=ServiceConfigurer.DICTIONARY_CACHE, key="#dictkey")
	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
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

	@Cacheable(value=ServiceConfigurer.DICTIONARY_CACHE, key="#dictKey")
	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
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

	@Cacheable(value=ServiceConfigurer.DICTIONARY_CACHE, key="#dictGroup")
	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
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
