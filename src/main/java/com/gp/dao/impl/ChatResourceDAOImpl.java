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
import com.gp.common.DataSourceHolder;
import com.gp.dao.ChatResourceDAO;
import com.gp.dao.info.ChatResourceInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;

@Component
public class ChatResourceDAOImpl extends DAOSupport implements ChatResourceDAO{

	Logger LOGGER = LoggerFactory.getLogger(ChatResourceDAOImpl.class);
	
	@Autowired
	public ChatResourceDAOImpl(@Qualifier(DataSourceHolder.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create(ChatResourceInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("INSERT INTO gp_chat_resc (")
			.append("rel_id,chat_id,resource_id,resource_type,")
			.append("modifier,last_modified")
			.append(") VALUES (")
			.append("?,?,?,?,")
			.append("?,?)");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				key.getId(), info.getChatId(),info.getResourceId(),info.getResourceType(),
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
		SQL.append("DELETE FROM gp_chat_resc ")
			.append("WHERE rel_id = ? ");
		
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
	public int update(ChatResourceInfo info, FilterMode mode,FlatColLocator... excludeCols) {
		Set<String> colset = FlatColumns.toColumnSet(excludeCols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		SQL.append("UPDATE gp_chat_resc SET ");
		
		if(columnCheck(mode, colset, "chat_id")){
			SQL.append("chat_id = ?,");
			params.add(info.getChatId());
		}
		if(columnCheck(mode, colset, "resource_id")){
			SQL.append("resource_id = ?,");
			params.add(info.getResourceId());
		}
		if(columnCheck(mode, colset, "resource_type")){
			SQL.append("resource_type = ?,");
			params.add(info.getResourceType());
		}
		
		SQL.append("modifier = ?,last_modified = ? ")
			.append("WHERE rel_id = ? ");
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
	public ChatResourceInfo query(InfoId<?> id) {
		String SQL = "SELECT * FROM gp_chat_resc "
				+ "WHERE rel_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		}
		ChatResourceInfo ainfo = jtemplate.queryForObject(SQL, params, ChatResourceMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
