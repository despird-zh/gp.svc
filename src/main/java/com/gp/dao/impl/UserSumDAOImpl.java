package com.gp.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
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
import com.gp.dao.UserSumDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.dao.info.UserSumInfo;

@Component("usersumDAO")
public class UserSumDAOImpl extends DAOSupport implements UserSumDAO{

	static Logger LOGGER = LoggerFactory.getLogger(UserSumDAOImpl.class);
	
	@Autowired
	public UserSumDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create(UserSumInfo info) {
		
		StringBuffer SQL = new StringBuffer();
		
		SQL.append("INSERT INTO gp_user_summary (")
			.append("rel_id,account, task_sum, file_sum,")
			.append("share_sum, post_sum, modifier, last_modified")
			.append(")VALUES(")
			.append("?,?,?,?,")
			.append("?,?,?,?")
			.append(")");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				key.getId(),info.getAccount(),info.getTaskSummary(),info.getFileSummary(),
				info.getShareSummary(), info.getPostSummary(),info.getModifier(),info.getModifyDate()
		};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);

			// execute sql
		return jtemplate.update(SQL.toString(),params);
	}

	@Override
	public int delete(InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_user_summary ")
			.append("where rel_id = ? ");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			id.getId()
		};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = -1;

			rtv = jtemplate.update(SQL.toString(), params);

		return rtv;
	}

	@Override
	public int update(UserSumInfo info,FilterMode mode, FlatColLocator... excludeCols) {
		Set<String> colset = FlatColumns.toColumnSet(excludeCols);
		List<Object> params = new ArrayList<Object>();

		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_user_summary set ");
		
		if(columnCheck(mode, colset, "account")){
			SQL.append("account = ?,");
			params.add(info.getAccount());
		}
		if(columnCheck(mode, colset, "task_sum")){
			SQL.append("task_sum = ?,");
			params.add(info.getTaskSummary());
		}
		if(columnCheck(mode, colset, "share_sum")){
			SQL.append("share_sum = ?,");
			params.add(info.getShareSummary());
		}
		if(columnCheck(mode, colset, "post_sum")){
			SQL.append("post_sum = ?,");
			params.add(info.getPostSummary());
		}
		if(columnCheck(mode, colset, "file_sum")){
			SQL.append("file_sum = ?,");
			params.add(info.getFileSummary());
		}
		
		SQL.append("modifier = ?,last_modified = ? ")
		.append("where rel_id = ? ");
		params.add(info.getModifier());
		params.add(info.getModifyDate());
		params.add(info.getInfoId().getId());
		
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		int rtv = -1;

		rtv = jtemplate.update(SQL.toString(), params.toArray());

		return rtv;
	}

	@Override
	public UserSumInfo query(InfoId<?> id) {
		String SQL = "select * from gp_user_summary "
				+ "where rel_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);

		List<UserSumInfo>	ainfo = jtemplate.query(SQL, params, UserSumMapper);

		return CollectionUtils.isEmpty(ainfo) ? null : ainfo.get(0);
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public UserSumInfo queryByAccount(String account) {
		String SQL = "select * from gp_user_summary "
				+ "where account = ? ";
		
		Object[] params = new Object[]{				
				account
			};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);

		List<UserSumInfo>	ainfo = jtemplate.query(SQL, params, UserSumMapper);

		return CollectionUtils.isEmpty(ainfo) ? null : ainfo.get(0);
	}

}
