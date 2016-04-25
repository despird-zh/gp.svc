package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.dao.IdSettingDAO;
import com.gp.info.IdSettingInfo;
import com.gp.info.Identifier;

@Component("idSettingDao")
public class IdSettingDAOImpl extends DAOSupport implements IdSettingDAO{

	static Logger LOGGER = LoggerFactory.getLogger(IdSettingDAOImpl.class);
	@Autowired
	public IdSettingDAOImpl(DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public IdSettingInfo queryByIdKey( Identifier idKey) {

		String SQL = "select * from gp_identifier " +
				"where id_key = ?";
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + idKey);
		
		IdSettingInfo info = jtemplate.queryForObject(SQL, new String[]{idKey.getSchema()}, IdSettringMapper);
		
		return info;
	}

	@Override
	public void updateByIdKey(String modifier, Identifier idKey, Long nextValue) {
		String SQL = "update gp_identifier set curr_val = ? ,modifier = ?, last_modified = ? " +
				"where id_key = ?";
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + idKey);
		
		jtemplate.update(SQL, new Object[]{nextValue, modifier, new Date(System.currentTimeMillis()), idKey.getSchema()});
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public static RowMapper<IdSettingInfo> IdSettringMapper = new RowMapper<IdSettingInfo>(){

		@Override
		public IdSettingInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			IdSettingInfo idsetting = new IdSettingInfo();
			idsetting.setIdKey(rs.getString("id_key"));
			idsetting.setIdName(rs.getString("id_name"));
			idsetting.setCurrValue(rs.getLong("curr_val"));
			idsetting.setStepIncrement(rs.getInt("step_inc"));
			idsetting.setLength(rs.getInt("length"));
			idsetting.setPrefix(rs.getString("prefix"));
			idsetting.setPadChar(rs.getString("pad_char"));
			idsetting.setModifier(rs.getString("modifier"));
			idsetting.setModifyDate(rs.getTimestamp("last_modified"));
			
			return idsetting;
		}};
}
