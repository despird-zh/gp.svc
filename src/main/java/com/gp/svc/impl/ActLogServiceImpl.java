package com.gp.svc.impl;

import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.gp.common.ServiceContext;
import com.gp.dao.ActLogDAO;
import com.gp.dao.PseudoDAO;
import com.gp.exception.ServiceException;
import com.gp.info.ActLogInfo;
import com.gp.info.InfoId;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.pagination.PaginationHelper;
import com.gp.pagination.PaginationInfo;
import com.gp.svc.ActLogService;

@Service("actlogService")
public class ActLogServiceImpl implements ActLogService{
	
	static Logger LOGGER = LoggerFactory.getLogger(ActLogServiceImpl.class);
	
	@Autowired
	PseudoDAO pseudodao;
	
	@Autowired
	ActLogDAO actlogdao;
	
	@Override
	public PageWrapper<ActLogInfo> getWorkgroupActivityLogs(ServiceContext<?> svcctx, InfoId<Long> wid,
			PageQuery pagequery) throws ServiceException {
		
		StringBuffer SQL_COLS = new StringBuffer("SELECT a.* ");
		StringBuffer SQL_COUNT = new StringBuffer("SELECT count(a.log_id) ");
		StringBuffer SQL_FROM = new StringBuffer("FROM gp_activity_log a WHERE a.workgroup_id = ?  ORDER BY a.log_id desc");
		
		Object[] params = new Object[]{wid.getId()};
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		
		PageWrapper<ActLogInfo> pwrapper = new PageWrapper<ActLogInfo>();
		// get count sql scripts.
		String countsql = SQL_COUNT.append(SQL_FROM).toString();
		int totalrow = pseudodao.queryRowCount(jtemplate, countsql, params);
		// calculate pagination information, the page menu number is 5
		PaginationInfo pagination = new PaginationHelper(totalrow, 
				pagequery.getPageNumber(), 
				pagequery.getPageSize(), 5).getPaginationInfo();
		
		pwrapper.setPagination(pagination);
		
		// get page query sql
		String pagesql = pseudodao.getPageQuerySql(SQL_COLS.append(SQL_FROM).toString(), pagequery);
		
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + pagesql + " / params : " + ArrayUtils.toString(params));
		}
		List<ActLogInfo> result = null;
		try{
			result = jtemplate.query(pagesql, params, actlogdao.getRowMapper());
			pwrapper.setRows(result);
			
		}catch(DataAccessException dae){
			throw new ServiceException("Fail query activity logs", dae);
		}

		return pwrapper;
	}

	@Override
	public PageWrapper<ActLogInfo> getAccountActivityLogs(ServiceContext<?> svcctx, String account, PageQuery pagequery)
			throws ServiceException {
		
		StringBuffer SQL_COLS = new StringBuffer("SELECT a.* ");
		StringBuffer SQL_COUNT = new StringBuffer("SELECT count(a.log_id) ");
		StringBuffer SQL_FROM = new StringBuffer("FROM gp_activity_log a WHERE a.account = ?  ORDER BY a.log_id desc");
		
		Object[] params = new Object[]{account};
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		
		PageWrapper<ActLogInfo> pwrapper = new PageWrapper<ActLogInfo>();
		// get count sql scripts.
		String countsql = SQL_COUNT.append(SQL_FROM).toString();
		int totalrow = pseudodao.queryRowCount(jtemplate, countsql, params);
		// calculate pagination information, the page menu number is 5
		PaginationInfo pagination = new PaginationHelper(totalrow, 
				pagequery.getPageNumber(), 
				pagequery.getPageSize(), 5).getPaginationInfo();
		
		pwrapper.setPagination(pagination);
		
		// get page query sql
		String pagesql = pseudodao.getPageQuerySql(SQL_COLS.append(SQL_FROM).toString(), pagequery);
		
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + pagesql + " / params : " + ArrayUtils.toString(params));
		}
		List<ActLogInfo> result = null;
		try{
			result = jtemplate.query(pagesql, params, actlogdao.getRowMapper());
			pwrapper.setRows(result);
			
		}catch(DataAccessException dae){
			throw new ServiceException("Fail query activity logs", dae);
		}

		return pwrapper;
	}

	@Override
	public PageWrapper<ActLogInfo> getObjectActivityLogs(ServiceContext<?> svcctx, InfoId<?> objectId,
			PageQuery pagequery) throws ServiceException {
		
		StringBuffer SQL_COLS = new StringBuffer("SELECT a.* ");
		StringBuffer SQL_COUNT = new StringBuffer("SELECT count(a.log_id) ");
		StringBuffer SQL_FROM = new StringBuffer("FROM gp_activity_log a WHERE a.object_id = ?  ORDER BY a.log_id desc");
		
		Object[] params = new Object[]{objectId.toString()};
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		
		PageWrapper<ActLogInfo> pwrapper = new PageWrapper<ActLogInfo>();
		// get count sql scripts.
		String countsql = SQL_COUNT.append(SQL_FROM).toString();
		int totalrow = pseudodao.queryRowCount(jtemplate, countsql, params);
		// calculate pagination information, the page menu number is 5
		PaginationInfo pagination = new PaginationHelper(totalrow, 
				pagequery.getPageNumber(), 
				pagequery.getPageSize(), 5).getPaginationInfo();
		
		pwrapper.setPagination(pagination);
		
		// get page query sql
		String pagesql = pseudodao.getPageQuerySql(SQL_COLS.append(SQL_FROM).toString(), pagequery);
		
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + pagesql + " / params : " + ArrayUtils.toString(params));
		}
		List<ActLogInfo> result = null;
		try{
			result = jtemplate.query(pagesql, params, actlogdao.getRowMapper());
			pwrapper.setRows(result);
			
		}catch(DataAccessException dae){
			throw new ServiceException("Fail query activity logs", dae);
		}

		return pwrapper;
	}

}
