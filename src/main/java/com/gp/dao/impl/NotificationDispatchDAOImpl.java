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
import com.gp.config.ServiceConfigurer;
import com.gp.dao.NotificationDispatchDAO;
import com.gp.dao.info.NotificationDispatchInfo;
import com.gp.dao.info.NotificationInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;

@Component
public class NotificationDispatchDAOImpl extends DAOSupport implements NotificationDispatchDAO{

	static Logger LOGGER = LoggerFactory.getLogger(NotificationDispatchDAOImpl.class);
	
	@Autowired
	public NotificationDispatchDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create(NotificationDispatchInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("INSERT INTO gp_notification_dispatch (")
			.append("rel_id, notification_id, receiver,")
			.append("touch_flag,touch_time,")
			.append("modifier, last_modified")
			.append(")VALUES(")
			.append("?,?,?,")
			.append("?,?,")
			.append("?,?)");
		
		Object[] params = new Object[]{
				info.getInfoId().getId(),info.getNotificationId(),info.getReceiver(),
				info.getTouchFlag(),info.getTouchTime(),
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
		SQL.append("delete from gp_notification_dispatch ")
			.append("where rel_id = ?");
		
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
	public int update(NotificationDispatchInfo info, FilterMode mode, FlatColLocator... filterCols) {
		Set<String> colset = FlatColumns.toColumnSet(filterCols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_notification_dispatch set ");
		
		if(columnCheck(mode, colset, "notification_id")){
			SQL.append("notification_id = ?,");
			params.add(info.getNotificationId());
		}
		if(columnCheck(mode, colset, "touch_flag")){
			SQL.append("touch_flag = ?,");
			params.add(info.getTouchFlag());
		}
		if(columnCheck(mode, colset, "touch_time")){
			SQL.append("touch_time = ?,");
			params.add(info.getTouchTime());
		}
			
		SQL.append("modifier = ?, last_modified = ? ")
			.append("where rel_id = ?");
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
	public NotificationDispatchInfo query(InfoId<?> id) {
		String SQL = "select * from gp_notification_dispatch "
				+ "where rel_id = ?";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		NotificationDispatchInfo ainfo = jtemplate.queryForObject(SQL, params, NotifDispatchMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
