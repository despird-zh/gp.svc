package com.gp.svc.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.common.ServiceContext;
import com.gp.config.ServiceConfigurator;
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
	
	@Transactional(value = ServiceConfigurator.TRNS_MGR, readOnly = true)
	@Override
	public List<DictionaryInfo> getDictEntries(ServiceContext<?> svcctx) throws ServiceException {

		StringBuffer SQL = new StringBuffer();
		SQL.append("select * from gp_dictionary");
		
		JdbcTemplate template = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		
		RowMapper<DictionaryInfo> rmapper = dictionarydao.getRowMapper();
		
		try{
			List<DictionaryInfo> result = template.query(SQL.toString(), rmapper);
			return result;
		}catch(DataAccessException dae){
			throw new ServiceException("fail get dictions", dae);
		}		
	}

	@Transactional(value = ServiceConfigurator.TRNS_MGR, readOnly = true)
	@Override
	public List<DictionaryInfo> getDictEntries(ServiceContext<?> svcctx, String dictGroup) throws ServiceException {
		StringBuffer SQL = new StringBuffer();
		
		SQL.append("select * from gp_dictionary where dict_group = ?");
		
		JdbcTemplate template = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		
		RowMapper<DictionaryInfo> rmapper = dictionarydao.getRowMapper();
		
		Object[] parms = new Object[]{				
				dictGroup
			};
		
		try{
			List<DictionaryInfo> result = template.query(SQL.toString(),parms ,rmapper);
			return result;
		}catch(DataAccessException dae){
			throw new ServiceException("fail get dictions", dae);
		}
	}

	@Transactional(value = ServiceConfigurator.TRNS_MGR)
	@Override
	public boolean updateDictEntry(ServiceContext<?> svcctx, DictionaryInfo dictinfo) throws ServiceException {

		try{
			svcctx.setTraceInfo(dictinfo);
			return dictionarydao.update(dictinfo) > 0;
		}catch(DataAccessException dae){
			throw new ServiceException("fail update diction", dae);
		}
	}

	@Transactional(value = ServiceConfigurator.TRNS_MGR, readOnly = true)
	@Override
	public DictionaryInfo getDictEntry(ServiceContext<?> svcctx, InfoId<Long> dictId)throws ServiceException 
	{
		try{
			return dictionarydao.query( dictId);
		}catch(DataAccessException dae){
			throw new ServiceException("fail get diction", dae);
		}
	}
	
	@Transactional(value = ServiceConfigurator.TRNS_MGR, readOnly = true)
	@Override
	public DictionaryInfo getDictEntry(ServiceContext<?> svcctx, String dictKey) throws ServiceException {
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("select * from gp_dictionary where dict_key = ?");
		
		JdbcTemplate template = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		
		RowMapper<DictionaryInfo> rmapper = dictionarydao.getRowMapper();
		
		Object[] parms = new Object[]{				
				dictKey
			};
		
		try{
			DictionaryInfo result = template.queryForObject(SQL.toString(), parms, rmapper);			
			return result;
		}catch(DataAccessException dae){
			throw new ServiceException("fail get diction", dae);
		}
	}

}
