package com.gp.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.gp.dao.NotificationDAO;
import com.gp.dao.info.NotificationInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;

@Component
public class NotificationDAOImpl extends DAOSupport implements NotificationDAO{

	static Logger LOGGER = LoggerFactory.getLogger(NotificationDAOImpl.class);
	
	@Autowired
	public NotificationDAOImpl(@Qualifier(DataSourceHolder.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create(NotificationInfo info) {
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("INSERT INTO gp_notifications (")
			.append("notification_id, source_id, workgroup_id, resource_id,")
			.append("resource_type,operation,subject,quote_excerpt,")
			.append("excerpt,sender, send_time,")
			.append("modifier, last_modified ")
			.append(")VALUES(")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,")
			.append("?,?)");
		
		Object[] params = new Object[]{
				info.getInfoId().getId(),info.getSourceId(),info.getWorkgroupId(),info.getResourceId(),
				info.getResourceType(),info.getOperation(),info.getSubject(),info.getQuoteExcerpt(),
				info.getExcerpt(),info.getSender(),info.getSendTime(),
				info.getModifier(), info.getModifyDate()
		};

		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		
		return jtemplate.update(SQL.toString(),params);
	}

	@Override
	public int delete(InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_notifications ")
			.append("where notification_id = ?");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			id.getId()
		};
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(), params);
		return rtv;
	}

	@Override
	public int update(NotificationInfo info, FilterMode mode, FlatColLocator... filterCols) {
		Set<String> colset = FlatColumns.toColumnSet(filterCols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_notifications set ");
		
		if(columnCheck(mode, colset, "source_id")){
			SQL.append("source_id = ?,");
			params.add(info.getSourceId());
		}
		if(columnCheck(mode, colset, "workgroup_id")){
			SQL.append("workgroup_id = ?,");
			params.add(info.getWorkgroupId());
		}
		if(columnCheck(mode, colset, "resource_id")){
			SQL.append("resource_id = ?,");
			params.add(info.getResourceId());
		}
		if(columnCheck(mode, colset, "resource_type")){
			SQL.append("resource_type = ?,");
			params.add(info.getResourceType());
		}
		if(columnCheck(mode, colset, "operation")){
			SQL.append("operation = ?,");
			params.add(info.getOperation());
		}
		if(columnCheck(mode, colset, "subject")){
			SQL.append("subject = ?,");
			params.add(info.getSubject());
		}
		if(columnCheck(mode, colset, "quote_excerpt")){
			SQL.append("quote_excerpt = ?,");
			params.add(info.getQuoteExcerpt());
		}
		if(columnCheck(mode, colset, "excerpt")){
			SQL.append("excerpt = ?,");
			params.add(info.getExcerpt());
		}
		if(columnCheck(mode, colset, "sender")){
			SQL.append("sender = ?,");
			params.add(info.getSender());
		}
		if(columnCheck(mode, colset, "send_time")){
			SQL.append("send_time = ?,");
			params.add(info.getSendTime());
		}
	
		SQL.append("modifier = ?, last_modified = ? ")
			.append("where notification_id = ?");
		params.add(info.getModifier());
		params.add(info.getModifyDate());
		params.add(info.getInfoId().getId());
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, params);
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		int rtv = jtemplate.update(SQL.toString(),params.toArray());
		return rtv;
	}

	@Override
	public NotificationInfo query(InfoId<?> id) {
		String SQL = "select * from gp_notifications "
				+ "where notification_id = ?";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		NotificationInfo ainfo = jtemplate.queryForObject(SQL, params, NotifcationMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
