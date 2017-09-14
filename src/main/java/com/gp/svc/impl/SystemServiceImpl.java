package com.gp.svc.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.common.GeneralConstants;
import com.gp.common.IdKey;
import com.gp.common.ServiceContext;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.common.DataSourceHolder;
import com.gp.dao.PseudoDAO;
import com.gp.dao.SysOptionDAO;
import com.gp.dao.UserDAO;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.dao.info.SysOptionInfo;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.pagination.PaginationHelper;
import com.gp.pagination.PaginationInfo;
import com.gp.svc.SystemService;
import com.gp.util.DateTimeUtils;

/**
 * Defines all method related with system option, it runs a internal timer to 
 * to refresh and cache the system options queried from database.
 * the default refresh interval is 30 seconds.
 * 
 * @author gary diao
 * @version 0.1 2015-1-1
 **/

@Service
public class SystemServiceImpl implements SystemService{

	Logger LOGGER = LoggerFactory.getLogger(SystemServiceImpl.class);

	@Autowired
	SysOptionDAO sysoptiondao;
	
	@Autowired
	UserDAO userdao;
	
	@Autowired
	PseudoDAO pseudodao;
	
	@Autowired
	public void setCacheManager(CacheManager cacheManager) {
		cache = cacheManager.getCache(DataSourceHolder.SYSSETTING_CACHE);
	}

	Cache cache = null;

	
	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public List<SysOptionInfo> getOptions(ServiceContext svcctx) throws ServiceException {
		try{
			return sysoptiondao.queryAll();
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query", dae, "system options");
		}
	}

	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public List<SysOptionInfo> getOptions(ServiceContext svcctx, String groupKey) throws ServiceException {
		StringBuffer SQL = new StringBuffer("SELECT * FROM gp_sys_options ");
		Object[] params = null;
		if(StringUtils.isNotBlank(groupKey)){
			
			SQL.append("WHERE opt_group = ? ");
			params = new Object[]{groupKey};
		}
		JdbcTemplate jdbctemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);	
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		try{
			List<SysOptionInfo> rows = jdbctemplate.query(SQL.toString(), params, SysOptionDAO.SysOptionMapper);	
			return rows;
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "system options", groupKey);
		}
	}

	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public PageWrapper<SysOptionInfo> getOptions(ServiceContext svcctx, String groupKey, PageQuery pagequery) throws ServiceException {
		
		List<SysOptionInfo> rows = null;
		StringBuffer SQL = new StringBuffer("SELECT * FROM gp_sys_options ");
		Object[] params = null;
		if(StringUtils.isNotBlank(groupKey)){
			
			SQL.append("WHERE opt_group = ? ");
			params = new Object[]{groupKey};
		}
		
		PageWrapper<SysOptionInfo> pwrapper = new PageWrapper<SysOptionInfo>();
		JdbcTemplate jdbctemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);		
		String countsql = StringUtils.replace(SQL.toString(), "SELECT * FROM", "SELECT count(1) FROM");
		
		int totalrow = 0;
		if(params != null){
			totalrow = pseudodao.queryRowCount(jdbctemplate, countsql, params);
		}else{
			
			totalrow = pseudodao.queryRowCount(countsql);
		}
		PaginationInfo pagination = new PaginationHelper(totalrow, 
				pagequery.getPageNumber(), 
				pagequery.getPageSize(), 5).getPaginationInfo();
		pwrapper.setPagination(pagination);
		
		String pagesql = pseudodao.getPageQuerySql(SQL.toString(), pagequery);
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + pagesql + " / params : " + ArrayUtils.toString(params));
		}
		
		try{
			rows = jdbctemplate.query(pagesql, params, SysOptionDAO.SysOptionMapper);	
			pwrapper.setRows(rows);
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query", dae, "system options");
		}
		
		return pwrapper;
	}
	
	@Transactional(DataSourceHolder.TRNS_MGR)
	@Override
	public boolean updateOption(ServiceContext svcctx, String optKey, String value) throws ServiceException {
		
		SysOptionInfo opt = sysoptiondao.queryByKey( optKey);
		opt.setOptionValue(value);
		opt.setModifier(svcctx.getPrincipal().getAccount());
		opt.setModifyDate(DateTimeUtils.now());
		try{
			boolean m = sysoptiondao.update( opt,FilterMode.NONE) > 0;
			if(null != cache){
				cache.put(IdKey.GP_SYS_OPTIONS.name() + GeneralConstants.KEYS_SEPARATOR + optKey, opt);
			}
			return m;
		}catch(DataAccessException dae){
			throw new ServiceException("excp.update.with", dae, "system options", "key="+optKey);
		}

	}

	@Transactional(DataSourceHolder.TRNS_MGR)
	@CacheEvict(value=DataSourceHolder.SYSSETTING_CACHE, key="#oKey")
	@Override
	public boolean updateOption(ServiceContext svcctx, InfoId<Long> oKey, String value) throws ServiceException {
		
		SysOptionInfo opt = sysoptiondao.query( oKey);
		opt.setOptionValue(value);
		svcctx.setTraceInfo(opt);
		try{
			boolean m = sysoptiondao.update(opt,FilterMode.NONE) > 0;
			
			if(null != cache){
				cache.put(IdKey.GP_SYS_OPTIONS.name() + GeneralConstants.KEYS_SEPARATOR + opt.getOptionKey(), opt);
			}	
			return m;
		}catch(DataAccessException dae){
			throw new ServiceException("excp.update.with", dae, "system options", "key="+oKey);
		}

	}

	@Override
	public SysOptionInfo getOption(ServiceContext svcctx, String oKey) throws ServiceException {
		
		if(null != cache){
			SysOptionInfo rtv = cache.get(IdKey.GP_SYS_OPTIONS.name() + GeneralConstants.KEYS_SEPARATOR + oKey, SysOptionInfo.class);
			if(rtv == null){
				rtv = sysoptiondao.queryByKey( oKey);
				if(rtv != null){
				
					cache.put(IdKey.GP_SYS_OPTIONS.name() + GeneralConstants.KEYS_SEPARATOR + oKey, rtv);
				}
			}
			
			return rtv;
		}
		try{
			return sysoptiondao.queryByKey( oKey);
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "system options", "key="+oKey);
		}
	}

	@Override
	public SysOptionInfo getOption(ServiceContext svcctx, InfoId<Long> oKey) throws ServiceException {
		try{
			return sysoptiondao.query( oKey);
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "system options", "key="+oKey);
		}
	}

	@Override
	public List<String> getOptionGroups(ServiceContext svcctx) throws ServiceException {
		StringBuffer SQL = new StringBuffer("select distinct opt_group from gp_sys_options");
		
		JdbcTemplate jdbctemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);	
		
		try{
			List<String> rows = jdbctemplate.query(SQL.toString(), new RowMapper<String>(){

				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					
					return rs.getString("opt_group");
				}});	
			return rows;
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query", dae, "system option groups");
		}
	}


}
