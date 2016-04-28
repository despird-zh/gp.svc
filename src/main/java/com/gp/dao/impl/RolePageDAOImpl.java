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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.dao.PageDAO;
import com.gp.dao.RolePageDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.FlatColumn;
import com.gp.info.InfoId;
import com.gp.info.RolePageInfo;

@Component("rolepagedao")
public class RolePageDAOImpl extends DAOSupport implements RolePageDAO{

	Logger LOGGER = LoggerFactory.getLogger(RolePageDAOImpl.class);
	
	@Autowired
	public RolePageDAOImpl(DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create(RolePageInfo info) {
		
		StringBuffer SQL = new StringBuffer("INSERT INTO gp_role_page (rel_id, page_id,role_id,");
		StringBuffer SQL_PARAMS = new StringBuffer("?,?,?,");
		
		List<Object> params = new ArrayList<Object>();
		params.add(info.getInfoId().getId());
		params.add(info.getPageId());
		params.add(info.getRoleId());
		
		for(Map.Entry<FlatColLocator, Boolean> entry: info.getPermMap().entrySet()){
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
		String SQL = "DELETE FROM gp_role_page WHERE rel_id = ?";
		
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
	public int update(RolePageInfo info) {
		
		StringBuffer SQL = new StringBuffer("UPDATE gp_role_page (page_id=?, role_id=?");

		List<Object> params = new ArrayList<Object>();
		params.add(info.getPageId());
		params.add(info.getRoleId());

		
		for(Map.Entry<FlatColLocator, Boolean> entry: info.getPermMap().entrySet()){
			SQL.append(entry.getKey().getColumn()).append(" = ?,");
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
	public RolePageInfo query(InfoId<?> id) {
		
		String SQL = "SELECT * FROM gp_role_page WHERE rel_id = ?";
		
		Object[] params = new Object[]{
				id.getId()
		};
		
		JdbcTemplate jtemplate = getJdbcTemplate(JdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		
		List<RolePageInfo> pinfos = jtemplate.query(SQL, params, ROLEPAGE_MAPPER);
		
		return CollectionUtils.isEmpty(pinfos)? null : pinfos.get(0);
	}

	@Override
	public RowMapper<RolePageInfo> getRowMapper() {
		
		return ROLEPAGE_MAPPER;
	}
	
	public static RowMapper<RolePageInfo> ROLEPAGE_MAPPER = new RowMapper<RolePageInfo>(){

		@Override
		public RolePageInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			RolePageInfo rpage = new RolePageInfo();
			
			InfoId<Integer> mid = IdKey.PAGE.getInfoId(rs.getInt("rel_id"));
			rpage.setInfoId(mid);
			
			rpage.setPageId(rs.getInt("page_id"));
			rpage.setRoleId(rs.getInt("role_id"));
			
			for(int i = 1; i <= PageDAO.COLUMN_COUNT ; i++){
				Boolean val = rs.getBoolean("act_perm_"+i);
				
				FlatColumn col = new FlatColumn("act_perm_", i);
				rpage.putColValue(col, val);			
				
			}
			
			rpage.setModifier(rs.getString("modifier"));
			rpage.setModifyDate(rs.getTimestamp("last_modified"));
			return rpage;
		}
		
	};
	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
