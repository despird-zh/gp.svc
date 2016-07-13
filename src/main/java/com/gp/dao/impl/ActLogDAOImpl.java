package com.gp.dao.impl;

import java.util.ArrayList;
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

import com.gp.common.FlatColumns;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.ActLogDAO;
import com.gp.info.ActLogInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
@Component("activitylogDAO")
public class ActLogDAOImpl extends DAOSupport implements ActLogDAO{

	Logger LOGGER = LoggerFactory.getLogger(ActLogDAOImpl.class);
	
	@Autowired
	public ActLogDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
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
	public int update(ActLogInfo info, FilterMode mode, FlatColLocator ...cols) {
		
		StringBuffer SQL = new StringBuffer();
		Set<String> colset = FlatColumns.toColumnSet(cols);
		List<Object> params = new ArrayList<Object>();
		SQL.append("update gp_activity_log set ");
		if(columnCheck(mode, colset, "workgroup_id")){
			SQL.append("workgroup_id = ?,");
			params.add(info.getWorkgroupId());
		}
		if(columnCheck(mode, colset, "account")){
			SQL.append("account = ?,");
			params.add(info.getAccount());
		}
		if(columnCheck(mode, colset, "user_name")){
			SQL.append("user_name = ?,");
			params.add(info.getUserName());
		}
		if(columnCheck(mode, colset, "audit_id")){
			SQL.append("audit_id = ?,");
			params.add(info.getAuditId());
		}
		if(columnCheck(mode, colset, "activity_date")){
			SQL.append("activity_date = ?,");
			params.add(info.getActivityDate());
		}
		if(columnCheck(mode, colset, "activity")){
			SQL.append("activity = ?,");
			params.add(info.getActivity());
		}
		if(columnCheck(mode, colset, "object_id")){
			SQL.append("object_id = ?,");
			params.add(info.getObjectId());
		}
		if(columnCheck(mode, colset, "object_excerpt")){
			SQL.append("object_excerpt = ?,");
			params.add(info.getObjectExcerpt());
		}
		if(columnCheck(mode, colset, "predicate_id")){
			SQL.append("predicate_id = ?,");
			params.add(info.getPredicateId());
		}
		if(columnCheck(mode, colset, "predicate_excerpt")){
			SQL.append("predicate_excerpt = ?,");
			params.add(info.getPredicateExcerpt());
		}
		
		SQL.append("modifier = ?,");
		params.add(info.getModifier());
		SQL.append("last_modified = ? ");
		params.add(info.getModifyDate());

		SQL.append("where log_id = ? ");
		params.add(info.getInfoId().getId());
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + params.toString());
		}
		int rtv = jtemplate.update(SQL.toString(),params.toArray());
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
		List<ActLogInfo> ainfo = jtemplate.query(SQL, params, ActLogMapper);
		return ainfo.size()>0 ? ainfo.get(0):null;
	}
	
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
		List<ActLogInfo> infos = jtemplate.query(SQL, params, ActLogMapper);
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
		List<ActLogInfo> infos = jtemplate.query(SQL, params, ActLogMapper);
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
		List<ActLogInfo> infos = jtemplate.query(SQL, params, ActLogMapper);
		return infos;
	}

}
