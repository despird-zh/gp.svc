package com.gp.svc.impl;

import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.common.ServiceContext;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.OperationDAO;
import com.gp.dao.PseudoDAO;
import com.gp.dao.UserDAO;
import com.gp.exception.ServiceException;
import com.gp.dao.info.OperationInfo;
import com.gp.dao.info.UserInfo;
import com.gp.info.InfoId;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.pagination.PaginationHelper;
import com.gp.pagination.PaginationInfo;
import com.gp.svc.OperationService;

@Service
public class OperationServiceImpl implements OperationService {
	
	static Logger LOGGER = LoggerFactory.getLogger(OperationServiceImpl.class);
	
	@Autowired
	PseudoDAO pseudodao;
	
	@Autowired
	OperationDAO actlogdao;
	
	@Autowired
	UserDAO userdao;
	
	@Transactional(value=ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public PageWrapper<OperationInfo> getWorkgroupOperations(ServiceContext svcctx, InfoId<Long> wid,
														 PageQuery pagequery) throws ServiceException {
		
		StringBuffer SQL_COLS = new StringBuffer("SELECT a.* ");
		StringBuffer SQL_COUNT = new StringBuffer("SELECT count(a.oper_id) ");
		StringBuffer SQL_FROM = new StringBuffer("FROM gp_operations a WHERE a.workgroup_id = ?  ORDER BY a.oper_id desc");
		
		Object[] params = new Object[]{wid.getId()};
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		
		PageWrapper<OperationInfo> pwrapper = new PageWrapper<OperationInfo>();
		if(pagequery.isTotalCountEnable()){
			// get count sql scripts.
			String countsql = SQL_COUNT.append(SQL_FROM).toString();
			int totalrow = pseudodao.queryRowCount(jtemplate, countsql, params);
			// calculate pagination information, the page menu number is 5
			PaginationInfo pagination = new PaginationHelper(totalrow, 
					pagequery.getPageNumber(), 
					pagequery.getPageSize(), 5).getPaginationInfo();
			
			pwrapper.setPagination(pagination);
		}
		// get page query sql
		String pagesql = pseudodao.getPageQuerySql(SQL_COLS.append(SQL_FROM).toString(), pagequery);
		
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + pagesql + " / params : " + ArrayUtils.toString(params));
		}
		List<OperationInfo> result = null;
		try{
			result = jtemplate.query(pagesql, params, OperationDAO.ActLogMapper);
			pwrapper.setRows(result);
			
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae,"Workgroup's ActionLog", wid);
		}

		return pwrapper;
	}

	@Transactional(value=ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public PageWrapper<OperationInfo> getAccountOperations(ServiceContext svcctx, String account, PageQuery pagequery)
			throws ServiceException {
		
		StringBuffer SQL_COLS = new StringBuffer("SELECT a.* ");
		StringBuffer SQL_COUNT = new StringBuffer("SELECT count(a.oper_id) ");
		StringBuffer SQL_FROM = new StringBuffer("FROM gp_operations a WHERE a.subject = ?  ORDER BY a.oper_id desc");
		
		Object[] params = new Object[]{account};
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		
		PageWrapper<OperationInfo> pwrapper = new PageWrapper<OperationInfo>();
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
		List<OperationInfo> result = null;
		try{
			result = jtemplate.query(pagesql, params, OperationDAO.ActLogMapper);
			pwrapper.setRows(result);
			
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae,"Account's ActionLog", account);
		}

		return pwrapper;
	}

	@Transactional(value=ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public PageWrapper<OperationInfo> getObjectOperations(ServiceContext svcctx, InfoId<?> objectId,
													  PageQuery pagequery) throws ServiceException {
		
		StringBuffer SQL_COLS = new StringBuffer("SELECT a.* ");
		StringBuffer SQL_COUNT = new StringBuffer("SELECT count(a.oper_id) ");
		StringBuffer SQL_FROM = new StringBuffer("FROM gp_operations a WHERE a.object = ?  ORDER BY a.oper_id desc");
		
		Object[] params = new Object[]{objectId.toString()};
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		
		PageWrapper<OperationInfo> pwrapper = new PageWrapper<OperationInfo>();
		if(pagequery.isTotalCountEnable()){
			// get count sql scripts.
			String countsql = SQL_COUNT.append(SQL_FROM).toString();
			int totalrow = pseudodao.queryRowCount(jtemplate, countsql, params);
			// calculate pagination information, the page menu number is 5
			PaginationInfo pagination = new PaginationHelper(totalrow, 
					pagequery.getPageNumber(), 
					pagequery.getPageSize(), 5).getPaginationInfo();
			
			pwrapper.setPagination(pagination);
		}
		// get page query sql
		String pagesql = pseudodao.getPageQuerySql(SQL_COLS.append(SQL_FROM).toString(), pagequery);
		
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + pagesql + " / params : " + ArrayUtils.toString(params));
		}
		List<OperationInfo> result = null;
		try{
			result = jtemplate.query(pagesql, params, OperationDAO.ActLogMapper);
			pwrapper.setRows(result);
			
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae,"Resource's ActionLog", objectId);
		}

		return pwrapper;
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public void addOperation(OperationInfo operlog) throws ServiceException {

		try{
			UserInfo uinfo = userdao.queryByAccount(operlog.getSubject());
			operlog.setSubjectExcerpt(uinfo.getFullName());
			actlogdao.create(operlog);

		}catch(DataAccessException dae){

			throw new ServiceException("excp.create", dae, "Activity log");
		}
	}

}
