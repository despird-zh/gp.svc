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
import com.gp.config.ServiceConfigurer;
import com.gp.dao.MessageDispatchDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.info.MessageDispatchInfo;

@Component("messageDispatchDAO")
public class MessageDispatchDAOImpl extends DAOSupport implements MessageDispatchDAO{

	Logger LOGGER = LoggerFactory.getLogger(SourceDAOImpl.class);
	
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
	public int update( MessageDispatchInfo info, FlatColLocator ...exclcols) {
		StringBuffer SQL = new StringBuffer();
		Set<String> cols = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		SQL.append("update gp_message_dispatch set ");
		if(!cols.contains("message_id")){
			SQL.append("message_id = ?,");
			params.add(info.getMessageId());
		}
		if(!cols.contains("msg_content")){
			SQL.append("msg_content = ?,");
			params.add(info.getMessageContent());
		}
		if(!cols.contains("account")){
			SQL.append("account = ?,");
			params.add(info.getAccount());
		}
		if(!cols.contains("global_account")){
			SQL.append("global_account = ?, ");
			params.add(info.getGlobalAccount());
		}
		if(!cols.contains("touch_flag")){
			SQL.append("touch_flag = ?,");
			params.add(info.getTouchFlag());
		}
		if(!cols.contains("touch_time")){
			SQL.append("touch_time = ?, ");
			params.add(info.getTouchTime());
		}
		
		SQL.append("modifier = ?,last_modified = ? ")
		.append("where rel_id = ? ");
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

}
