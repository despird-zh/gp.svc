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
import com.gp.dao.MessageDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.info.MessageInfo;

@Component("messageDAO")
public class MessageDAOImpl extends DAOSupport implements MessageDAO{

	Logger LOGGER = LoggerFactory.getLogger(MessageDAOImpl.class);
	
	@Autowired
	public MessageDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( MessageInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_messages (")
			.append("source_id,workgroup_id,message_id,")
			.append("cabinet_id,resource_id,resource_type,operation,")
			.append("msg_dict_key, msg_params_json,  category,")
			.append("send_global_account, send_account,  reply_enable,")
			.append("modifier,last_modified")
			.append(")values(")
			.append("?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,")
			.append("?,?,?,")
			.append("?,?)");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				info.getSourceId(),info.getWorkgroupId(),key.getId(),
				info.getCabinetId(),info.getResourceId(),info.getResourceType(),info.getOperation(),
				info.getMsgDictKey(),info.getMsgParams(),info.getCategory(),
				info.getSendGlobalAccount(),info.getSendAccount(),info.getReplyEnable(),
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
		SQL.append("delete from gp_messages ")
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
	public int update(MessageInfo info, FlatColLocator ...exclcols) {
		Set<String> cols = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_messages set ");
		if(!cols.contains("workgroup_id")){
			SQL.append("workgroup_id = ?,");
			params.add(info.getWorkgroupId());
		}
		if(!cols.contains("cabinet_id")){
			SQL.append("cabinet_id = ?,");
			params.add(info.getCabinetId());
		}
		if(!cols.contains("resource_id")){
			SQL.append("resource_id = ?,");
			params.add(info.getResourceId());
		}
		if(!cols.contains("resource_type")){
			SQL.append("resource_type = ?,");
			params.add(info.getResourceType());
		}
		if(!cols.contains("operation")){
			SQL.append("operation = ?,");
			params.add(info.getOperation());
		}
		if(!cols.contains("msg_dict_key")){
			SQL.append("msg_dict_key = ?, ");
			params.add(info.getMsgDictKey());
		}
		if(!cols.contains("msg_params_json")){
			SQL.append("msg_params_json = ?,  ");
			params.add(info.getMsgParams());
		}
		if(!cols.contains("category")){
			SQL.append("category = ?,");
			params.add(info.getCategory());
		}
		if(!cols.contains("source_id")){
			SQL.append("source_id = ?, ");
			params.add(info.getSourceId());
		}
		if(!cols.contains("send_global_account")){
		SQL.append("send_global_account = ?, ");
		params.add(info.getSendGlobalAccount());
		}
		if(!cols.contains("send_account")){
		SQL.append("send_account = ?,  ");
		params.add(info.getSendAccount());
		}
		if(!cols.contains("reply_enable")){
		SQL.append("reply_enable = ?,");
		params.add(info.getReplyEnable());
		}
		
		SQL.append("modifier = ?,last_modified = ? ")
			.append("where message_id = ? ");
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
	public MessageInfo query( InfoId<?> id) {
		String SQL = "select * from gp_messages "
				+ "where message_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		}
		MessageInfo ainfo = jtemplate.queryForObject(SQL, params, MessageMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}



}
