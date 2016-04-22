package com.gp.svc.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.gp.common.Instances.State;
import com.gp.common.ServiceContext;
import com.gp.dao.InstanceDAO;
import com.gp.dao.PseudoDAO;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.info.InstanceInfo;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.pagination.PaginationHelper;
import com.gp.pagination.PaginationInfo;
import com.gp.svc.InstanceService;

@Service("instanceService")
public class InstanceServiceImpl implements InstanceService{

	Logger LOGGER = LoggerFactory.getLogger(InstanceServiceImpl.class);
	
	@Autowired
	private InstanceDAO instancedao;

	@Autowired
	private PseudoDAO pseudodao;
	
	@Override
	public InstanceInfo getInstnaceInfo(ServiceContext<?> svcctx, InfoId<Integer> id) throws ServiceException {
		
		try{
			
			return instancedao.query(id);
			
		}catch(DataAccessException dae){
			throw new ServiceException("fail get instance", dae);
		}
	}

	@Override
	public InstanceInfo getInstnace(ServiceContext<?> svcctx, String entity, String node) throws ServiceException {
		
		try{
			
			return instancedao.queryByCodes(entity, node);
			
		}catch(DataAccessException dae){
			throw new ServiceException("fail get instance", dae);
		}
	}
	
	@Override
	public boolean saveInstnace(ServiceContext<?> svcctx, InstanceInfo instance) throws ServiceException {

		try{
			svcctx.setTraceInfo(instance);
			int cnt = instancedao.update(instance);
			return cnt > 0;
		}catch(DataAccessException dae){
			throw new ServiceException("fail save instance", dae);
		}
	}

	@Override
	public List<InstanceInfo> getInstances(ServiceContext<?> svcctx, String instancename)
			throws ServiceException {
		
		List<InstanceInfo> rtv = null;
		StringBuffer SQL = new StringBuffer("SELECT * FROM gp_instances where instance_id > 0 ");
		
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNotBlank(instancename)){
			SQL.append(" and (instance_name like :inst_name or abbr like :abbr or short_name like :short_name) ");
			
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
			rtv = jtemplate.query(SQL.toString(), params, instancedao.getRowMapper());	
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("fail query instance", dae);
		}

		return rtv;
	}

	@Override
	public PageWrapper<InstanceInfo> getInstances(ServiceContext<?> svcctx, String instancename, PageQuery pagequery)
			throws ServiceException {
		
		List<InstanceInfo> rtv = null;
		StringBuffer SQL = new StringBuffer("SELECT * FROM gp_instances where instance_id > 0 ");

		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNotBlank(instancename)){
			SQL.append(" and (instance_name like :inst_name or abbr like :abbr or short_name like :short_name) ");
			
			String likeconds = "%" + StringUtils.trim(instancename) + "%";
		    params.put("inst_name", likeconds);
		    params.put("abbr", likeconds);
		    params.put("short_name", likeconds);
		}
		
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		
		PageWrapper<InstanceInfo> pwrapper = new PageWrapper<InstanceInfo>();
		// get count sql scripts.
		String countsql = StringUtils.replaceOnce(SQL.toString(), "SELECT * FROM", "SELECT COUNT(*) FROM");
		int totalrow = pseudodao.queryRowCount(jtemplate, countsql, params);
		// calculate pagination information, the page menu number is 5
		PaginationInfo pagination = new PaginationHelper(totalrow, 
				pagequery.getPageNumber(), 
				pagequery.getPageSize(), 5).getPaginationInfo();
		
		pwrapper.setPagination(pagination);
		
		// get page query sql
		String pagesql = pseudodao.getPageQuerySql(SQL.toString(), pagequery);
		
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + pagesql + " / params : " + ArrayUtils.toString(params));
		}
		try{
			rtv = jtemplate.query(pagesql, params, instancedao.getRowMapper());	
			pwrapper.setRows(rtv);
		}catch(DataAccessException dae){
			
			throw new ServiceException("fail query instance", dae);
		}

		return pwrapper;
	}

	@Override
	public boolean changeInstanceState(ServiceContext<?> svcctx, Integer instanceId, State state) throws ServiceException {

		try{
			
			return instancedao.updateState(instanceId, state.name()) > 0;
		}catch(DataAccessException dae){
			
			throw new ServiceException("fail query instance", dae);
		}

	}

	@Override
	public boolean addExtInstnace(ServiceContext<?> svcctx, InstanceInfo instance) throws ServiceException {
		try{
			svcctx.setTraceInfo(instance);
			int cnt = instancedao.create(instance);
			return cnt > 0;
		}catch(DataAccessException dae){
			throw new ServiceException("fail save instance", dae);
		}
	}
}
