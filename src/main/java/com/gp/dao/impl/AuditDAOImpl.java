package com.gp.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.gp.common.DataSourceHolder;
import com.gp.common.FlatColumns;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.dao.AuditDAO;
import com.gp.dao.info.AuditInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;

@Component
public class AuditDAOImpl extends DAOSupport implements AuditDAO{

	Logger LOGGER = LoggerFactory.getLogger(AuditDAOImpl.class);
	
	@Autowired
	public AuditDAOImpl(@Qualifier(DataSourceHolder.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}

	@Override
	public int create(AuditInfo info){
		StringBuffer SQL = new StringBuffer();
		
		SQL.append("insert into gp_audits (")
			.append(" workgroup_id, audit_id,")
			.append("client, host, app, version, ")
			.append(" operation, subject,")
			.append("predicate_json,object,state,message,")
			.append("audit_time,elapse_time,modifier,last_modified) values (")
			.append("?,?,")
			.append("?,?,?,?,")
			.append("?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,?)");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
			info.getWorkgroupId(),key.getId(),
			info.getClient(), info.getHost(), info.getApp(), info.getVersion(),
			info.getOperation(),info.getSubject(),
			info.getPredicates(),info.getTarget(),info.getState(),info.getMessage(),
			info.getAuditDate(),info.getElapseTime(),info.getModifier(),new Date(System.currentTimeMillis())
		};
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		return jtemplate.update(SQL.toString(),params);

	}

	@Override
	public int delete(InfoId<?> oid){

		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_audits ")
			.append("where audit_id = ? ");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			oid.getId()
		};
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(), params);
		return rtv;
	}

	@Override
	public int update(AuditInfo info , FilterMode mode, FlatColLocator ...exclcols){
		Set<String> colset = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		
		SQL.append("update gp_audits set ");
		
		if(columnCheck(mode, colset, "workgroup_id")){
			SQL.append("workgroup_id = ?,");
			params.add(info.getWorkgroupId());
		}
		if(columnCheck(mode, colset, "operation")){
			SQL.append("operation = ?, ");
			params.add(info.getOperation());
		}
		if(columnCheck(mode, colset, "subject")){
			SQL.append("subject = ?,");
			params.add(info.getSubject());
		}
		if(columnCheck(mode, colset, "client")){
			SQL.append("client = ?,");
			params.add(info.getClient());
		}
		if(columnCheck(mode, colset, "host")){
			SQL.append(" host = ?, ");
			params.add(info.getHost());
		}
		if(columnCheck(mode, colset, "app")){
			SQL.append("app = ?, ");
			params.add(info.getApp());
		}
		if(columnCheck(mode, colset, "version")){
			SQL.append("version = ?,");
			params.add(info.getVersion());
		}
		if(columnCheck(mode, colset, "predicate_json")){
			SQL.append("predicate_json = ?,");
			params.add(info.getPredicates());
		}
		if(columnCheck(mode, colset, "object")){
			SQL.append("object = ?,");
			params.add(info.getTarget());
		}
		if(columnCheck(mode, colset, "state")){
			SQL.append("state = ?,");
			params.add(info.getState());
		}
		if(columnCheck(mode, colset, "message")){
			SQL.append("message=?,");
			params.add(info.getMessage());
		}
		if(columnCheck(mode, colset, "audit_time")){
			SQL.append("audit_time=?,");
			params.add(info.getAuditDate());
		}
		if(columnCheck(mode, colset, "elapse_time")){
			SQL.append("elapse_time = ?,");
			params.add(info.getElapseTime());
		}
		
		SQL.append("modifier = ?,last_modified = ? ");
		SQL.append("where audit_id = ? ");
		params.add(info.getModifier());
		params.add(info.getModifyDate());
		params.add(info.getInfoId().getId());
		
	
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(), params.toArray());
		return rtv;
	}

	@Override
	public AuditInfo query(InfoId<?> oid){
		String SQL = "select * from gp_audits "
				+ "where audit_id = ?";
		
		Object[] params = new Object[]{				
				oid.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		AuditInfo ainfo = jtemplate.queryForObject(SQL, params, AuditMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	

}
