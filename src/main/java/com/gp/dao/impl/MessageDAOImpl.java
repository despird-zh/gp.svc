package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.MessageDAO;
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
	public int update(MessageInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_messages set ")
		.append("workgroup_id = ?,")
		.append("cabinet_id = ?,resource_id = ?,resource_type = ?,operation = ?,")
		.append("msg_dict_key = ?, msg_params_json = ?,  category = ?,source_id = ?, ")
		.append("send_global_account = ?, send_account = ?,  reply_enable = ?,")
		.append("modifier = ?,last_modified = ? ")
		.append("where message_id = ? ");
		
		Object[] params = new Object[]{
				info.getWorkgroupId(),
				info.getCabinetId(),info.getResourceId(),info.getResourceType(),info.getOperation(),
				info.getMsgDictKey(),info.getMsgParams(),info.getCategory(),info.getSourceId(),
				info.getSendGlobalAccount(),info.getSendAccount(),info.getReplyEnable(),
				info.getModifier(),info.getModifyDate(),
				info.getInfoId().getId()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(),params);
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

	public static RowMapper<MessageInfo> MessageMapper = new RowMapper<MessageInfo>(){

		@Override
		public MessageInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			MessageInfo info = new MessageInfo();
			InfoId<Long> id = IdKey.MESSAGE.getInfoId(rs.getLong("message_id"));
			info.setInfoId(id);
			
			info.setSourceId(rs.getInt("source_id"));
			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setCabinetId(rs.getLong("cabinet_id"));
			info.setResourceId(rs.getLong("resource_id"));
			info.setResourceType(rs.getString("resource_type"));
			info.setOperation(rs.getString("operation"));
			info.setSendAccount(rs.getString("send_account"));
			info.setReplyEnable(rs.getBoolean("reply_enable"));
			info.setCategory(rs.getString("category"));
			info.setMsgDictKey(rs.getString("msg_dict_key"));
			info.setMsgParams(rs.getString("msg_params_json"));
			info.setSendGlobalAccount(rs.getString("send_global_account"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};

	@Override
	public RowMapper<MessageInfo> getRowMapper() {
		// TODO Auto-generated method stub
		return MessageMapper;
	}
}
