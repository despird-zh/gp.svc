package com.gp.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.gp.common.FlatColumns;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.ChatMessageDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.dao.info.ChatMessageInfo;

@Component
public class ChatMessageDAOImpl extends DAOSupport implements ChatMessageDAO{

	Logger LOGGER = LoggerFactory.getLogger(ChatMessageDAOImpl.class);
	
	@Autowired
	public ChatMessageDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( ChatMessageInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("INSERT INTO gp_chat_msgs (")
			.append("message_id,chat_id,msg_type, msg_content,")
			.append("resource_id,resource_type,sender,send_time,")
			.append("modifier,last_modified")
			.append(") VALUES (")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?)");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				key.getId(), info.getChatId(),info.getMessageType(),info.getMessageContent(),
				info.getResourceId(),info.getResourceType(),info.getSender(),info.getSendTime(),
				info.getModifier(),info.getModifyDate()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		}
		return jtemplate.update(SQL.toString(),params);

	}

	@Override
	public int delete( InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_chat_msgs ")
			.append("where message_id = ? ");
		
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
	public int update(ChatMessageInfo info, FilterMode mode,FlatColLocator ...exclcols) {
		Set<String> colset = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		SQL.append("UPDATE gp_chat_msgs SET ");
		
		if(columnCheck(mode, colset, "chat_id")){
			SQL.append("chat_id = ?,");
			params.add(info.getChatId());
		}
		if(columnCheck(mode, colset, "msg_type")){
			SQL.append("msg_type = ?,");
			params.add(info.getMessageType());
		}
		if(columnCheck(mode, colset, "resource_id")){
			SQL.append("resource_id = ?,");
			params.add(info.getResourceId());
		}
		if(columnCheck(mode, colset, "resource_type")){
			SQL.append("resource_type = ?,");
			params.add(info.getResourceType());
		}
		if(columnCheck(mode, colset, "msg_content")){
			SQL.append("msg_content = ?,");
			params.add(info.getMessageContent());
		}
		if(columnCheck(mode, colset, "sender")){
			SQL.append("sender = ?, ");
			params.add(info.getSender());
		}
		if(columnCheck(mode, colset, "send_time")){
			SQL.append("send_time = ?,  ");
			params.add(info.getSendTime());
		}
		
		SQL.append("modifier = ?,last_modified = ? ")
			.append("WHERE message_id = ? ");
		params.add(info.getModifier());
		params.add(info.getModifyDate());
		params.add(info.getInfoId().getId());
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, params);
		}
		int rtv = jtemplate.update(SQL.toString(),params.toArray());
		return rtv;
	}

	@Override
	public ChatMessageInfo query( InfoId<?> id) {
		String SQL = "SELECT * FROM gp_chat_msgs "
				+ "WHERE message_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		}
		ChatMessageInfo ainfo = jtemplate.queryForObject(SQL, params, MessageMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
