package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.config.ServiceConfigurator;
import com.gp.dao.UserRoleDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.FlatColumn;
import com.gp.info.InfoId;
import com.gp.info.UserRoleInfo;

@Component("userroledao")
public class UserRoleDAOImple extends DAOSupport implements UserRoleDAO{

	Logger LOGGER = LoggerFactory.getLogger(UserRoleDAOImple.class);
	
	@Autowired
	public UserRoleDAOImple(@Qualifier(ServiceConfigurator.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create(UserRoleInfo info) {
		
		StringBuffer SQL = new StringBuffer("INSERT INTO gp_user_role (rel_id,user_id,");
		StringBuffer SQL_PARAMS = new StringBuffer("?,?,");
		
		List<Object> params = new ArrayList<Object>();
		params.add(info.getInfoId().getId());
		params.add(info.getUserId());
		
		for(Map.Entry<FlatColLocator, Integer> entry: info.getRoleMap().entrySet()){
			SQL.append(entry.getKey().getColumn()).append(",");
			SQL_PARAMS.append("?,");
			params.add(entry.getValue());
		}
		
		SQL.append("modifier, last_modified ) VALUES (").append(SQL_PARAMS).append("?,? )");
		params.add(info.getModifier());
		params.add(info.getModifyDate());
		
		JdbcTemplate jtemplate = getJdbcTemplate(JdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, params.toString());
		
		int cnt  = jtemplate.update(SQL.toString(), params.toArray());
		
		return cnt;
	}

	@Override
	public int delete(InfoId<?> id) {
		String SQL = "DELETE FROM gp_user_role WHERE rel_id = ?";
		
		Object[] params = new Object[]{
				id.getId()
		};
		
		JdbcTemplate jtemplate = getJdbcTemplate(JdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		
		int cnt  = jtemplate.update(SQL.toString(), params);
		
		return cnt;
	}

	@Override
	public int update(UserRoleInfo info) {
		
		StringBuffer SQL = new StringBuffer("UPDATE gp_user_role (user_id =?,");
	
		List<Object> params = new ArrayList<Object>();
		params.add(info.getUserId());
		
		for(Map.Entry<FlatColLocator, Integer> entry: info.getRoleMap().entrySet()){
			SQL.append(entry.getKey().getColumn()).append("=?,");
			params.add(entry.getValue());
		}
		
		SQL.append("modifier=?, last_modified=? WHERE rel_id=?");
		params.add(info.getModifier());
		params.add(info.getModifyDate());
		params.add(info.getInfoId().getId());
		
		JdbcTemplate jtemplate = getJdbcTemplate(JdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, params.toString());
		
		int cnt  = jtemplate.update(SQL.toString(), params.toArray());
		
		return cnt;
	}

	@Override
	public UserRoleInfo query(InfoId<?> id) {
		
		String SQL = "SELECT * FROM gp_user_role WHERE rel_id = ?";
		
		Object[] params = new Object[]{
				id.getId()
		};
		
		JdbcTemplate jtemplate = getJdbcTemplate(JdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		
		List<UserRoleInfo> pinfos = jtemplate.query(SQL, params, USERROLE_MAPPER);
		
		return CollectionUtils.isEmpty(pinfos)? null : pinfos.get(0);
	}
	
	public static RowMapper<UserRoleInfo> USERROLE_MAPPER = new RowMapper<UserRoleInfo>(){

		@Override
		public UserRoleInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserRoleInfo ur = new UserRoleInfo();
			
			InfoId<Long> mid = IdKey.USER_ROLE.getInfoId(rs.getLong("rel_id"));
			ur.setInfoId(mid);
			
			ur.setUserId(rs.getLong("user_id"));
		
			for(int i = 1; i <= COLUMN_COUNT ; i++){
				Integer val = rs.getInt("role_"+i);
				if(val > 0){
					FlatColumn col = new FlatColumn("role_", i);
					ur.putColValue(col, val);
				}				
			}
			
			ur.setModifier(rs.getString("modifier"));
			ur.setModifyDate(rs.getTimestamp("last_modified"));
			return ur;
		}
		
	};
	@Override
	public RowMapper<UserRoleInfo> getRowMapper() {
		
		return USERROLE_MAPPER;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
