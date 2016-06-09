package com.gp.svc.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.common.FlatColumns;
import com.gp.common.ServiceContext;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.DictionaryDAO;
import com.gp.dao.PseudoDAO;
import com.gp.exception.ServiceException;
import com.gp.info.DictionaryInfo;
import com.gp.info.InfoId;
import com.gp.svc.DictionaryService;

@Service("dictionaryService")
public class DictionaryServiceImpl implements DictionaryService{

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
		
		RowMapper<DictionaryInfo> rmapper = dictionarydao.getRowMapper();
		
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
		
		RowMapper<DictionaryInfo> rmapper = dictionarydao.getRowMapper();
		
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
			return dictionarydao.update(dictinfo) > 0;
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
		
		RowMapper<DictionaryInfo> rmapper = dictionarydao.getRowMapper();
		
		Object[] parms = new Object[]{				
				dictKey
			};
		
		try{
			DictionaryInfo result = template.queryForObject(SQL.toString(), parms, rmapper);			
			return result;
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "Dictionary", dictKey);
		}
	}

	@Cacheable(value=ServiceConfigurer.DICTIONARY_CACHE, key="#dictKey")
	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public DictionaryInfo getDictEntry(String dictKey) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("SELECT * FROM gp_dictionary WHERE dict_key = ?");
		JdbcTemplate template = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		RowMapper<DictionaryInfo> rmapper = dictionarydao.getRowMapper();
		Object[] parms = new Object[]{				
				dictKey
			};
		
		try{
			DictionaryInfo result = template.queryForObject(SQL.toString(), parms, rmapper);			
			return result;
		}catch(DataAccessException dae){
			dae.printStackTrace();
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
		RowMapper<DictionaryInfo> rmapper = dictionarydao.getRowMapper();
		
		Object[] parms = new Object[]{				
				dictGroup
			};
		
		try{
			List<DictionaryInfo> result = template.query(SQL.toString(), parms ,rmapper);
			return result;
		}catch(DataAccessException dae){
			dae.printStackTrace();
			return null;
		}
	}

	@Override
	public String getMessagePattern(Locale locale, String dictKey){
		
		DictionaryInfo dinfo = this.getDictEntry(dictKey);
		String msgptn = null;
		if(Locale.ENGLISH.equals(locale)){
			msgptn = dinfo.getLabel(FlatColumns.DICT_EN_US);
		}else if(Locale.SIMPLIFIED_CHINESE.equals(locale)){
			msgptn = dinfo.getLabel(FlatColumns.DICT_ZH_CN);
		}else if(Locale.FRANCE.equals(locale)){
			msgptn = dinfo.getLabel(FlatColumns.DICT_FR_FR);
		}else if(Locale.GERMAN.equals(locale)){
			msgptn = dinfo.getLabel(FlatColumns.DICT_DE_DE);
		}
		
		return msgptn;
	}
}
