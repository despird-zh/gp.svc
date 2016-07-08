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
import com.gp.config.ServiceConfigurer;
import com.gp.dao.ChatDAO;
import com.gp.info.ChatInfo;
import com.gp.info.ChatMessageInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;

@Component("chatDAO")
public class ChatDAOImpl extends DAOSupport implements ChatDAO{

	Logger LOGGER = LoggerFactory.getLogger(ChatDAOImpl.class);
	
	@Autowired
	public ChatDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create(ChatInfo info) {
		StringBuffer SQL = new StringBuffer();
		
		SQL.append("INSERT INTO gp_chats (")
			.append("chat_id,chat_type, sponsor,topic,")
			.append("mbr_group_id,create_time,")
			.append("modifier,last_modified")
			.append(") VALUES (")
			.append("?,?,?,?,")
			.append("?,?,")
			.append("?,?)");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				key.getId(), info.getChatType(),info.getSponsor(),info.getTopic(),
				info.getMemberGroupId(),info.getCreateTime(),
				info.getModifier(),info.getModifyDate()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		}
		return jtemplate.update(SQL.toString(),params);
	}

	@Override
	public int delete(InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("DELETE FROM gp_chats ")
			.append("WHERE chat_id = ? ");
		
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
	public int update(ChatInfo info, FlatColLocator... excludeCols) {
		Set<String> cols = FlatColumns.toColumnSet(excludeCols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		SQL.append("UPDATE gp_chats SET ");
		
		if(!cols.contains("chat_type")){
			SQL.append("chat_type = ?,  ");
			params.add(info.getChatType());
		}
		if(!cols.contains("sponsor")){
			SQL.append("sponsor = ?,  ");
			params.add(info.getSponsor());
		}
		if(!cols.contains("topic")){
			SQL.append("topic = ?,  ");
			params.add(info.getTopic());
		}
		if(!cols.contains("mbr_group_id")){
			SQL.append("mbr_group_id = ?,  ");
			params.add(info.getMemberGroupId());
		}
		if(!cols.contains("create_time")){
			SQL.append("create_time = ?,  ");
			params.add(info.getCreateTime());
		}
		
		SQL.append("modifier = ?,last_modified = ? ")
		.append("WHERE chat_id = ? ");
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
	public ChatInfo query(InfoId<?> id) {
		String SQL = "SELECT * FROM gp_chats "
				+ "WHERE chat_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		}
		ChatInfo ainfo = jtemplate.queryForObject(SQL, params, ChatMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
