package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.MessageDispatchDAO;
import com.gp.info.InfoId;
import com.gp.info.MessageDispatchInfo;

@Component("messageDispatchDAO")
public class MessageDispatchDAOImpl extends DAOSupport implements MessageDispatchDAO{

	Logger LOGGER = LoggerFactory.getLogger(InstanceDAOImpl.class);
	
	@Autowired
	public MessageDispatchDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( MessageDispatchInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_message_dispatch (")
			.append("rel_id, message_id, ")
			.append("msg_content, account, global_account, ")
			.append("touch_flag, touch_time, ")
			.append("modifier, last_modified")
			.append(")values(")
			.append("?,?,")
			.append("?,?,?,")
			.append("?,?,")
			.append("?,?)");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				key.getId(),info.getMessageId(),
				info.getMessageContent(),info.getAccount(),info.getGlobalAccount(),
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
		SQL.append("delete from gp_message_dispatch ")
			.append("where rel_id = ? ");
		
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
	public int update( MessageDispatchInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_message_dispatch set ")
		.append("message_id = ?,")
		.append("msg_content = ?,account = ?,global_account = ?, ")
		.append("touch_flag = ?, touch_time = ?, ")
		.append("modifier = ?,last_modified = ? ")
		.append("where rel_id = ? ");
		
		Object[] params = new Object[]{
				info.getMessageId(),
				info.getMessageContent(),info.getAccount(),info.getGlobalAccount(),
				info.getTouchFlag(),info.getTouchTime(),
				info.getModifier(),info.getModifyDate(),
				info.getInfoId().getId()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(),params);
		return rtv;
	}

	@Override
	public MessageDispatchInfo query( InfoId<?> id) {
		String SQL = "select * from gp_message_dispatch "
				+ "where rel_id = ?";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		MessageDispatchInfo ainfo = jtemplate.queryForObject(SQL, params, MessageDispatchMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public static RowMapper<MessageDispatchInfo> MessageDispatchMapper = new RowMapper<MessageDispatchInfo>(){

		@Override
		public MessageDispatchInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			MessageDispatchInfo info = new MessageDispatchInfo();
			InfoId<Long> id = IdKey.MESSAGE.getInfoId(rs.getLong("rel_id"));
			info.setInfoId(id);

			info.setMessageId(rs.getLong("message_id"));
			info.setMessageContent(rs.getString("msg_content"));
			info.setAccount(rs.getString("account"));
			info.setGlobalAccount(rs.getString("global_account"));
			info.setTouchFlag(rs.getBoolean("touch_flag"));
			info.setTouchTime(rs.getDate("touch_time"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};

	@Override
	public RowMapper<MessageDispatchInfo> getRowMapper() {
		
		return MessageDispatchMapper;
	}
}
