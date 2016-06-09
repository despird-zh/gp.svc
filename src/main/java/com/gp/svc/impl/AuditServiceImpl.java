package com.gp.svc.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.common.ServiceContext;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.AuditDAO;
import com.gp.dao.PseudoDAO;
import com.gp.exception.ServiceException;
import com.gp.info.AuditInfo;
import com.gp.info.InfoId;
import com.gp.svc.AuditService;

@Service("auditService")
public class AuditServiceImpl implements AuditService{

	Logger LOGGER = LoggerFactory.getLogger(AuditServiceImpl.class);
	
	@Autowired
	AuditDAO auditdao;
	
	@Autowired
	PseudoDAO pseudodao;
	
	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public List<AuditInfo> getAudits(ServiceContext svcctx, String subject, String object, String operation) throws ServiceException {
		
		List<Object> parmlist = new ArrayList<Object>();
		
		StringBuffer sqlbuf = new StringBuffer();
		sqlbuf.append("SELECT * FROM gp_audit WHERE 1=1 ")
			.append("and source_id = ? ");
		
		// query principal related data only.
		parmlist.add(svcctx.getPrincipal().getSourceId());
		
		if(StringUtils.isNotBlank(subject)){
			
			sqlbuf.append("and subject = ?");
			parmlist.add(subject);
		}
		
		if(StringUtils.isNotBlank(object)){
			sqlbuf.append("and object like ?");
			parmlist.add(object);
		}
		
		if(StringUtils.isNotBlank(operation)){
			sqlbuf.append("and operation like ?");
			parmlist.add(operation);
		}
		
		JdbcTemplate template = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = parmlist.toArray();
		RowMapper<AuditInfo> mapper = auditdao.getRowMapper();
		
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : {} / params : {}", sqlbuf.toString(), ArrayUtils.toString(params));
		}
		try{
			List<AuditInfo> result = template.query(sqlbuf.toString(), params, mapper);
		
			return result;
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query", dae,"Audit log");
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean deleteAudit(ServiceContext svcctx, InfoId<Long> id) throws ServiceException {

		try{
			int cnt = auditdao.delete( id);
			return cnt > 0;
		}catch(DataAccessException dae){
			throw new ServiceException("excp.delete.with", dae,"Audit", id);
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean addAudit(ServiceContext svcctx, AuditInfo ainfo) throws ServiceException {
		try{
			return auditdao.create( ainfo) > 0;
			
		}catch(DataAccessException dae){
			throw new ServiceException("excp.create", dae,"Audit");
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean purgeAudits(ServiceContext svcctx, String subject, String objectType, Date reservedate)
			throws ServiceException {
		
		List<Object> parmlist = new ArrayList<Object>();
		StringBuffer sqlbuf = new StringBuffer();
		sqlbuf.append("DELETE FROM gp_audits WHERE 1=1 ")
			.append("and instance_id = ? ");
		
		// query principal related data only.
		parmlist.add(svcctx.getPrincipal().getSourceId());
		
		if(StringUtils.isNotBlank(subject)){
			
			sqlbuf.append("and subject = ?");
			parmlist.add(subject);
		}
		
		if(StringUtils.isNotBlank(objectType)){
			sqlbuf.append("and object like ?");
			parmlist.add(objectType);
		}
		
		if(null != reservedate){
			sqlbuf.append("and audit_time < ?");
			parmlist.add(reservedate);
		}
		
		JdbcTemplate template = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = parmlist.toArray();
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : {} / params : {}", sqlbuf.toString(), ArrayUtils.toString(params));
		}
		
		try{
			int cnt = template.update(sqlbuf.toString(), params);
			return cnt > 0;
		}catch(DataAccessException dae){
			throw new ServiceException("excp.delete", dae,"Audit");
		}
	}

}
