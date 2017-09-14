package com.gp.dao.impl;

import java.util.Date;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.gp.common.DataSourceHolder;
import com.gp.dao.IdSettingDAO;
import com.gp.dao.info.IdSettingInfo;
import com.gp.info.Identifier;

@Component
public class IdSettingDAOImpl extends DAOSupport implements IdSettingDAO{

	static Logger LOGGER = LoggerFactory.getLogger(IdSettingDAOImpl.class);
	@Autowired
	public IdSettingDAOImpl(@Qualifier(DataSourceHolder.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public IdSettingInfo queryByIdKey( Identifier idKey) {

		String SQL = "select * from gp_identifier " +
				"where id_key = ?";
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + idKey.getSchema().toLowerCase());
		
		IdSettingInfo info = jtemplate.queryForObject(SQL, new String[]{idKey.getSchema().toLowerCase()}, IdSettringMapper);
		
		return info;
	}

	@Override
	public int updateByIdKey(String modifier, Identifier idKey, Long nextValue) {
		String SQL = "update gp_identifier set curr_val = ? ,modifier = ?, last_modified = ? " +
				"where id_key = ?";
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
				nextValue, modifier, new Date(System.currentTimeMillis()), idKey.getSchema().toLowerCase()
		};
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		
		int cnt = jtemplate.update(SQL, params);
		return cnt;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
