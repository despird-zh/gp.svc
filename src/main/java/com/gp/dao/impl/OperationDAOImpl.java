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
import com.gp.common.DataSourceHolder;
import com.gp.dao.OperationDAO;
import com.gp.dao.info.OperationInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
@Component
public class OperationDAOImpl extends DAOSupport implements OperationDAO {

	Logger LOGGER = LoggerFactory.getLogger(OperationDAOImpl.class);
	
	@Autowired
	public OperationDAOImpl(@Qualifier(DataSourceHolder.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create(OperationInfo info) {

		StringBuffer SQL = new StringBuffer();
		
		SQL.append("insert into gp_operations (")
			.append("oper_id, workgroup_id, subject, subject_excerpt,")
			.append("audit_id, operation_time, operation, operation_excerpt, object,")
			.append("object_excerpt, predicate, predicate_excerpt, ")
			.append("modifier, last_modified ")
			.append(")values(")
			.append("?,?,?,?,")
			.append("?,?,?,?,?,")
			.append("?,?,?,")
			.append("?,?)");
		
		InfoId<Long> key = info.getInfoId();
		
		Object[] params = new Object[]{
				key.getId(),info.getWorkgroupId(),info.getSubject(), info.getSubjectExcerpt(),
				info.getAuditId(),info.getOperationTime(),info.getOperation(),info.getOperationExcerpt(), info.getObject(),
				info.getObjectExcerpt(),info.getPredicate(),info.getPredicateExcerpt(),
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
		SQL.append("delete from gp_operations ")
			.append("where oper_id = ? ");
		
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
	public int update(OperationInfo info, FilterMode mode, FlatColLocator ...cols) {
		
		StringBuffer SQL = new StringBuffer();
		Set<String> colset = FlatColumns.toColumnSet(cols);
		List<Object> params = new ArrayList<Object>();
		SQL.append("update gp_operations set ");
		if(columnCheck(mode, colset, "workgroup_id")){
			SQL.append("workgroup_id = ?,");
			params.add(info.getWorkgroupId());
		}
		if(columnCheck(mode, colset, "subject")){
			SQL.append("subject = ?,");
			params.add(info.getSubject());
		}
		if(columnCheck(mode, colset, "subject_excerpt")){
			SQL.append("subject_excerpt = ?,");
			params.add(info.getSubjectExcerpt());
		}
		if(columnCheck(mode, colset, "audit_id")){
			SQL.append("audit_id = ?,");
			params.add(info.getAuditId());
		}
		if(columnCheck(mode, colset, "operation_time")){
			SQL.append("operation_time = ?,");
			params.add(info.getOperationTime());
		}
		if(columnCheck(mode, colset, "operation")){
			SQL.append("operation = ?,");
			params.add(info.getOperation());
		}
		if(columnCheck(mode, colset, "operation_excerpt")){
			SQL.append("operation_excerpt = ?,");
			params.add(info.getOperationExcerpt());
		}
		if(columnCheck(mode, colset, "object")){
			SQL.append("object = ?,");
			params.add(info.getObject());
		}
		if(columnCheck(mode, colset, "object_excerpt")){
			SQL.append("object_excerpt = ?,");
			params.add(info.getObjectExcerpt());
		}
		if(columnCheck(mode, colset, "predicate")){
			SQL.append("predicate = ?,");
			params.add(info.getPredicate());
		}
		if(columnCheck(mode, colset, "predicate_excerpt")){
			SQL.append("predicate_excerpt = ?,");
			params.add(info.getPredicateExcerpt());
		}
		
		SQL.append("modifier = ?,");
		params.add(info.getModifier());
		SQL.append("last_modified = ? ");
		params.add(info.getModifyDate());

		SQL.append("where oper_id = ? ");
		params.add(info.getInfoId().getId());
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + params.toString());
		}
		int rtv = jtemplate.update(SQL.toString(),params.toArray());
		return rtv;
	}

	@Override
	public OperationInfo query(InfoId<?> id) {
		String SQL = "SELECT * FROM aq_operations WHERE oper_id = ?";
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		List<OperationInfo> ainfo = jtemplate.query(SQL, params, ActLogMapper);
		return ainfo.size()>0 ? ainfo.get(0):null;
	}
	
	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<OperationInfo> queryByAccount(String account) {
		String SQL = "SELECT * FROM aq_operations WHERE subject = ? ORDER BY oper_id desc";
		Object[] params = new Object[]{				
				account
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		List<OperationInfo> infos = jtemplate.query(SQL, params, ActLogMapper);
		return infos;
	}

	@Override
	public List<OperationInfo> queryByWorkgroup(InfoId<Long> wid) {
		String SQL = "SELECT * FROM aq_operations WHERE workgroup_id = ? ORDER BY oper_id desc";
		Object[] params = new Object[]{				
				wid.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		List<OperationInfo> infos = jtemplate.query(SQL, params, ActLogMapper);
		return infos;
	}

	@Override
	public List<OperationInfo> queryByObject(InfoId<Long> objectid) {
		String SQL = "SELECT * FROM aq_operation_log WHERE object = ? ORDER BY log_id desc";
		Object[] params = new Object[]{				
				objectid.toString()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		List<OperationInfo> infos = jtemplate.query(SQL, params, ActLogMapper);
		return infos;
	}

}
