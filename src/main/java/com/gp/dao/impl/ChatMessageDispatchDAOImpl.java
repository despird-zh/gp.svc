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

import com.gp.common.DataSourceHolder;
import com.gp.common.FlatColumns;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.dao.ChatMessageDispatchDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.dao.info.ChatMessageDispatchInfo;

@Component
public class ChatMessageDispatchDAOImpl extends DAOSupport implements ChatMessageDispatchDAO{

	Logger LOGGER = LoggerFactory.getLogger(SourceDAOImpl.class);
	
	@Autowired
	public ChatMessageDispatchDAOImpl(@Qualifier(DataSourceHolder.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( ChatMessageDispatchInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("INSERT INTO gp_chat_dispatch (")
			.append("rel_id, message_id, receiver,")
			.append("touch_flag, touch_time, ")
			.append("modifier, last_modified")
			.append(") VALUES (")
			.append("?,?,?,")
			.append("?,?,")
			.append("?,?)");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				key.getId(),info.getMessageId(), info.getReceiver(),
				info.getTouchFlag(),info.getTouchTime(),
				info.getModifier(),info.getModifyDate()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		return jtemplate.update(SQL.toString(),params);

	}

	@Override
	public int delete( InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("DELETE FROM gp_chat_dispatch ")
			.append("WHERE rel_id = ? ");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			id.getId()
		};
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(), params);
		return rtv;
	}

	@Override
	public int update( ChatMessageDispatchInfo info, FilterMode mode,FlatColLocator ...exclcols) {
		StringBuffer SQL = new StringBuffer();
		Set<String> colset = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		SQL.append("UPDATE gp_chat_dispatch SET ");
		
		if(columnCheck(mode, colset, "message_id")){
			SQL.append("message_id = ?,");
			params.add(info.getMessageId());
		}
		if(columnCheck(mode, colset, "receiver")){
			SQL.append("receiver = ?,");
			params.add(info.getReceiver());
		}

		if(columnCheck(mode, colset, "touch_flag")){
			SQL.append("touch_flag = ?,");
			params.add(info.getTouchFlag());
		}
		if(columnCheck(mode, colset, "touch_time")){
			SQL.append("touch_time = ?, ");
			params.add(info.getTouchTime());
		}
		
		SQL.append("modifier = ?,last_modified = ? ")
		.append("WHERE rel_id = ? ");
		params.add(info.getModifier());
		params.add(info.getModifyDate());
		params.add(info.getInfoId().getId());
	
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(),params.toArray());
		return rtv;
	}

	@Override
	public ChatMessageDispatchInfo query( InfoId<?> id) {
		String SQL = "SELECT * FROM gp_chat_dispatch "
				+ "WHERE rel_id = ?";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		ChatMessageDispatchInfo ainfo = jtemplate.queryForObject(SQL, params, MessageDispatchMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
