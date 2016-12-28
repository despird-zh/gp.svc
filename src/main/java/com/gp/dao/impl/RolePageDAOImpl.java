package com.gp.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.gp.common.FlatColumns;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.RolePageDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.dao.info.RolePageInfo;

@Component
public class RolePageDAOImpl extends DAOSupport implements RolePageDAO{

	Logger LOGGER = LoggerFactory.getLogger(RolePageDAOImpl.class);
	
	@Autowired
	public RolePageDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
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
	public int update(RolePageInfo info,FilterMode mode, FlatColLocator ...exclcols) {
		Set<String> colset = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		
		StringBuffer SQL = new StringBuffer("UPDATE gp_role_page SET ");
		
		if(columnCheck(mode, colset, "page_id")){
			SQL.append("page_id=?,");
			params.add(info.getPageId());
		}
		if(columnCheck(mode, colset, "role_id")){
			SQL.append(" role_id=?,");
			params.add(info.getRoleId());
		}
		
		for(Map.Entry<FlatColLocator, Boolean> entry: info.getPermMap().entrySet()){
			if(!columnCheck(mode, colset, entry.getKey().getColumn())) continue;
			
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
		
		List<RolePageInfo> pinfos = jtemplate.query(SQL, params, RolePageMapper);
		
		return CollectionUtils.isEmpty(pinfos)? null : pinfos.get(0);
	}
	
	
	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public int update(InfoId<Integer> roleId, InfoId<Integer> pageId, Map<FlatColLocator, Boolean> perms) {
		
		StringBuffer SQL = new StringBuffer("UPDATE gp_role_page SET ");

		List<Object> params = new ArrayList<Object>();

		
		for(Map.Entry<FlatColLocator, Boolean> entry: perms.entrySet()){
			SQL.append(entry.getKey().getColumn()).append(" = ?,");
			params.add(entry.getValue());
		}
		
		SQL.append(" last_modified=? WHERE page_id=?, role_id=?");
		params.add(new Date(System.currentTimeMillis()));
		params.add(pageId.getId());
		params.add(roleId.getId());

		JdbcTemplate jtemplate = getJdbcTemplate(JdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, params.toString());
		
		int cnt  = jtemplate.update(SQL.toString(), params.toArray());
		return cnt;
	}

}
