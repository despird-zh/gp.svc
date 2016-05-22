package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.dao.ActLogDAO;
import com.gp.info.ActLogInfo;
import com.gp.info.InfoId;

public class ActLogDAOImpl extends DAOSupport implements ActLogDAO{

	Logger LOGGER = LoggerFactory.getLogger(ActLogDAOImpl.class);
	
	@Autowired
	public ActLogDAOImpl(DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create(ActLogInfo info) {

		StringBuffer SQL = new StringBuffer();
		
		SQL.append("insert into gp_activity_log (")
			.append("log_id,workgroup_id ,account ,user_name ,")
			.append("audit_id,activity_date , activity ,object_id,")
			.append("object_excerpt , predicate_id, predicate_excerpt, ")
			.append("modifier, last_modified ")
			.append(")values(")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,")
			.append("?,?)");
		
		InfoId<Long> key = info.getInfoId();
		
		Object[] params = new Object[]{
				key.getId(),info.getWorkgroupId(),info.getAccount(),info.getUserName(),
				info.getAuditId(),info.getActivityDate(),info.getActivity(),info.getObjectId(),
				info.getObjectExcerpt(),info.getPredicateId(),info.getPredicateExcerpt(),
				info.getModifier(),info.getModifyDate()
				
		};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		int cnt = jtemplate.update(SQL.toString(),params);
		return cnt;
	}

	@Override
	public int delete(InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_activity_log ")
			.append("where log_id = ? ");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			id.getId()
		};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(), params);
		return rtv;
	}

	@Override
	public int update(ActLogInfo info) {
		
		StringBuffer SQL = new StringBuffer();
		
		SQL.append("update gp_activity_log set ")
			.append("workgroup_id = ?,account =?,user_name = ? ,")
			.append("audit_id = ?,activity_date = ? , activity = ?,object_id=?,")
			.append("object_excerpt = ?, predicate_id = ?, predicate_excerpt = ?, ")
			.append("modifier = ?, last_modified = ? ")
			.append("where log_id = ? ");
		
		Object[] params = new Object[]{
				info.getWorkgroupId(),info.getAccount(),info.getUserName(),
				info.getAuditId(),info.getActivityDate(),info.getActivity(),info.getObjectId(),
				info.getObjectExcerpt(),info.getPredicateId(),info.getPredicateExcerpt(),
				info.getModifier(),info.getModifyDate(),
				info.getInfoId().getId()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(),params);
		return rtv;
	}

	@Override
	public ActLogInfo query(InfoId<?> id) {
		String SQL = "SELECT * FROM aq_actibity_log WHERE log_id = ?";
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		List<ActLogInfo> ainfo = jtemplate.query(SQL, params, ROW_MAPPER);
		return ainfo.size()>0 ? ainfo.get(0):null;
	}

	@Override
	public RowMapper<ActLogInfo> getRowMapper() {
		
		return ROW_MAPPER;
	}

	public static RowMapper<ActLogInfo> ROW_MAPPER = new RowMapper<ActLogInfo>(){

		@Override
		public ActLogInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			ActLogInfo actlog = new ActLogInfo();
			
			InfoId<Long> logid = IdKey.ACT_LOG.getInfoId(rs.getLong("log_id"));
			actlog.setInfoId(logid);
			actlog.setAccount(rs.getString("account"));
			actlog.setActivity(rs.getString("activity"));
			actlog.setActivityDate(rs.getTimestamp("activity_date"));
			actlog.setUserName(rs.getString("user_name"));
			actlog.setAuditId(rs.getLong("audit_id"));
			actlog.setObjectId(rs.getString("object_id"));
			actlog.setObjectExcerpt(rs.getString("object_excerpt"));
			actlog.setPredicateId(rs.getString("predicate_id"));
			actlog.setPredicateExcerpt(rs.getString("predicate_excerpt"));
			actlog.setWorkgroupId(rs.getLong("workgroup_id"));
			
			actlog.setModifier(rs.getString("modifier"));
			actlog.setModifyDate(rs.getTimestamp("last_modified"));
			return actlog;
		}};
	
	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<ActLogInfo> queryByAccount(String account) {
		String SQL = "SELECT * FROM aq_actibity_log WHERE account = ? ORDER BY log_id desc";
		Object[] params = new Object[]{				
				account
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		List<ActLogInfo> infos = jtemplate.query(SQL, params, ROW_MAPPER);
		return infos;
	}

	@Override
	public List<ActLogInfo> queryByWorkgroup(InfoId<Long> wid) {
		String SQL = "SELECT * FROM aq_actibity_log WHERE account = ? ORDER BY log_id desc";
		Object[] params = new Object[]{				
				wid.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		List<ActLogInfo> infos = jtemplate.query(SQL, params, ROW_MAPPER);
		return infos;
	}

	@Override
	public List<ActLogInfo> queryByObject(InfoId<Long> objectid) {
		String SQL = "SELECT * FROM aq_actibity_log WHERE object_id = ? ORDER BY log_id desc";
		Object[] params = new Object[]{				
				objectid.toString()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		List<ActLogInfo> infos = jtemplate.query(SQL, params, ROW_MAPPER);
		return infos;
	}

}
