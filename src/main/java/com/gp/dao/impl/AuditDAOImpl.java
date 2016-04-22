package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.dao.AuditDAO;
import com.gp.info.AuditInfo;
import com.gp.info.InfoId;

@Component("auditDAO")
public class AuditDAOImpl extends DAOSupport implements AuditDAO{

	Logger LOGGER = LoggerFactory.getLogger(AuditDAOImpl.class);
	
	@Autowired
	public AuditDAOImpl(DataSource dataSource) {
		setDataSource(dataSource);
	}

	@Override
	public int create(AuditInfo info){
		StringBuffer SQL = new StringBuffer();
		
		SQL.append("insert into gp_audits (")
			.append(" workgroup_id, audit_id,")
			.append("client, host, app, version, ")
			.append(" verb, subject,")
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
			info.getVerb(),info.getSubject(),
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
	public int update(AuditInfo info ){
		
		StringBuffer SQL = new StringBuffer();
		
		SQL.append("update gp_audits set ")
		.append("workgroup_id = ?,")
		.append("verb = ?, subject = ?,")
		.append("client = ?, host = ?, app = ?, version = ?,")
		.append("predicate_json = ?,object = ?,state = ?,message=?,")
		.append("audit_time=?,elapse_time = ?,modifier = ?,last_modified = ? ")
		.append("where ")
		.append(" audit_id = ? ");
		
		Object[] params = new Object[]{
				info.getWorkgroupId(),
				info.getVerb(),info.getSubject(),
				info.getClient(), info.getHost(), info.getApp(), info.getVersion(),
				info.getPredicates(),info.getTarget(),info.getState(),info.getMessage(),
				info.getAuditDate(),info.getElapseTime(),info.getModifier(),new Date(System.currentTimeMillis()),
				info.getInfoId().getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(), params);
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
	
	public static RowMapper<AuditInfo> AuditMapper = new RowMapper<AuditInfo>(){

		@Override
		public AuditInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		
			AuditInfo info = new AuditInfo();

			InfoId<Long> id = IdKey.AUDIT.getInfoId(rs.getLong("audit_id"));
			info.setInfoId(id);
			
			info.setWorkgroupId(rs.getLong("workgroup_id"));

			info.setClient(rs.getString("client"));
			info.setHost(rs.getString("host"));
			info.setApp(rs.getString("app"));
			info.setVersion(rs.getString("version"));
			
			info.setVerb(rs.getString("verb"));
			info.setSubject(rs.getString("subject"));
			info.setPredicates(rs.getString("predicate_json"));
			info.setTarget(rs.getString("object"));
			info.setState(rs.getString("state"));
			info.setMessage(rs.getString("message"));
			info.setAuditDate(rs.getTimestamp("audit_time"));
			info.setElapseTime(rs.getLong("elapse_time"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
		
	};
	
	@Override
	public RowMapper<AuditInfo> getRowMapper() {
		
		return AuditMapper;
	}
}
