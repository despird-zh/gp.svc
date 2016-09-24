package com.gp.svc.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.common.Sources.State;
import com.gp.config.ServiceConfigurer;
import com.gp.common.ServiceContext;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.dao.SourceDAO;
import com.gp.dao.PseudoDAO;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.dao.info.SourceInfo;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.pagination.PaginationHelper;
import com.gp.pagination.PaginationInfo;
import com.gp.svc.SourceService;

@Service
public class SourceServiceImpl implements SourceService{

	Logger LOGGER = LoggerFactory.getLogger(SourceServiceImpl.class);
	
	@Autowired
	private SourceDAO sourcedao;

	@Autowired
	private PseudoDAO pseudodao;
	
	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public SourceInfo getSource(ServiceContext svcctx, InfoId<Integer> id) throws ServiceException {
		
		try{
			
			return sourcedao.query(id);
			
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "source", id);
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public SourceInfo getSource(ServiceContext svcctx, String entity, String node) throws ServiceException {
		
		try{
			
			return sourcedao.queryByCodes(entity, node);
			
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "source", "entity:"+entity+"/node:"+node);
		}
	}
	
	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean saveSource(ServiceContext svcctx, SourceInfo instance) throws ServiceException {

		try{
			svcctx.setTraceInfo(instance);
			int cnt = sourcedao.update(instance,FilterMode.NONE);
			return cnt > 0;
		}catch(DataAccessException dae){
			throw new ServiceException("excp.update", dae, "source");
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public List<SourceInfo> getSources(ServiceContext svcctx, String instancename)
			throws ServiceException {
		
		List<SourceInfo> rtv = null;
		StringBuffer SQL = new StringBuffer("SELECT * FROM gp_sources where source_id > 0 ");
		
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNotBlank(instancename)){
			SQL.append(" and (source_name like :inst_name or abbr like :abbr or short_name like :short_name) ");
			
			String likeconds = "%" + StringUtils.trim(instancename) + "%";
		    params.put("inst_name", likeconds);
		    params.put("abbr", likeconds);
		    params.put("short_name", likeconds);
		}
		
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);

		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		try{
			rtv = jtemplate.query(SQL.toString(), params, SourceDAO.SourceMapper);	
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query", dae, "source");
		}

		return rtv;
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public PageWrapper<SourceInfo> getSources(ServiceContext svcctx, String instancename, PageQuery pagequery)
			throws ServiceException {
		
		List<SourceInfo> rtv = null;
		StringBuffer SQL = new StringBuffer("SELECT * FROM gp_sources where source_id > 0 ");

		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNotBlank(instancename)){
			SQL.append(" and (source_name like :inst_name or abbr like :abbr or short_name like :short_name) ");
			
			String likeconds = "%" + StringUtils.trim(instancename) + "%";
		    params.put("inst_name", likeconds);
		    params.put("abbr", likeconds);
		    params.put("short_name", likeconds);
		}
		
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		
		PageWrapper<SourceInfo> pwrapper = new PageWrapper<SourceInfo>();
		if(pagequery.isTotalCountEnable()){
			// get count sql scripts.
			String countsql = StringUtils.replaceOnce(SQL.toString(), "SELECT * FROM", "SELECT COUNT(*) FROM");
			int totalrow = pseudodao.queryRowCount(jtemplate, countsql, params);
			// calculate pagination information, the page menu number is 5
			PaginationInfo pagination = new PaginationHelper(totalrow, 
					pagequery.getPageNumber(), 
					pagequery.getPageSize(), 5).getPaginationInfo();
			
			pwrapper.setPagination(pagination);
		}
		// get page query sql
		String pagesql = pseudodao.getPageQuerySql(SQL.toString(), pagequery);
		
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + pagesql + " / params : " + ArrayUtils.toString(params));
		}
		try{
			rtv = jtemplate.query(pagesql, params, SourceDAO.SourceMapper);	
			pwrapper.setRows(rtv);
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query", dae, "source");
		}

		return pwrapper;
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean changeSourceState(ServiceContext svcctx, InfoId<Integer> instanceId, State state) throws ServiceException {

		try{
			
			return sourcedao.updateState(instanceId, state.name()) > 0;
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.update.with", dae, "source state", instanceId);
		}

	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean addExtSource(ServiceContext svcctx, SourceInfo instance) throws ServiceException {
		try{
			svcctx.setTraceInfo(instance);
			int cnt = sourcedao.create(instance);
			return cnt > 0;
		}catch(DataAccessException dae){
			throw new ServiceException("excp.create", dae, "external source");
		}
	}
	
	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public Map<String, SourceInfo> getAccountSources(ServiceContext svcctx, List<String> accounts) throws ServiceException {
		
		final Map<String, SourceInfo> rtv = new HashMap<String, SourceInfo>();
		
		StringBuffer SQL_COLS = new StringBuffer("SELECT a.account ,b.* ");
		StringBuffer SQL_FROM = new StringBuffer("FROM gp_users a ")
				.append("LEFT JOIN gp_sources b ON a.source_id = b.source_id ")
				.append("WHERE 1 = 1 ");
		Map<String,Object> params = new HashMap<String,Object>();

			SQL_FROM.append(" AND a.account in( :accounts )");
			params.put("accounts", accounts);
		
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		String querysql = SQL_COLS.append(SQL_FROM).toString();
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + querysql + " / params : " + ArrayUtils.toString(params));
		}

		try{	
			jtemplate.query(querysql, params, new RowCallbackHandler(){
				@Override
				public void processRow(ResultSet arg0) throws SQLException {
					SourceInfo inst = SourceDAO.SourceMapper.mapRow(arg0, 0);
					String account = arg0.getString("account");
					
					rtv.put(account, inst);
				}});
			
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query", dae, "account's source");
		}

		return rtv;
	}
}
