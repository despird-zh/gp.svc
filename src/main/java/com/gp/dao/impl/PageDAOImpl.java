package com.gp.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.gp.dao.PageDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.dao.info.PageInfo;

@Component("pagedao")
public class PageDAOImpl extends DAOSupport implements PageDAO {

	Logger LOGGER = LoggerFactory.getLogger(PageDAOImpl.class);
	
	@Autowired
	public PageDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create(PageInfo info) {
		
		StringBuffer SQL = new StringBuffer("INSERT INTO gp_pages (page_id,page_name,");
		SQL.append("module,descr,page_abbr,");
		StringBuffer SQL_PARAMS = new StringBuffer("?,?,?,?,?,");
		
		List<Object> params = new ArrayList<Object>();
		params.add(info.getInfoId().getId());
		params.add(info.getPageName());
		params.add(info.getModule());
		params.add(info.getDescription());
		params.add(info.getPageAbbr());
		
		for(Map.Entry<FlatColLocator, String> entry: info.getActionMap().entrySet()){
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
		
		String SQL = "DELETE FROM gp_pages WHERE page_id = ?";
		
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
	public int update(PageInfo info,FilterMode mode, FlatColLocator ...exclcols) {
		Set<String> colset = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer("UPDATE gp_pages SET page_name=?,");
	
		if(columnCheck(mode, colset, "page_name")){
			SQL.append("page_name=?,");
			params.add(info.getPageName());
		}
		if(columnCheck(mode, colset, "module")){
			SQL.append("module=?,");
			params.add(info.getModule());
		}
		if(columnCheck(mode, colset, "descr")){
			SQL.append("descr=?,");
			params.add(info.getDescription());
		}
		if(columnCheck(mode, colset, "page_abbr")){
			SQL.append("page_abbr=?,");
			params.add(info.getPageAbbr());
		}
		
		for(Map.Entry<FlatColLocator, String> entry: info.getActionMap().entrySet()){
			if(!columnCheck(mode, colset,entry.getKey().getColumn())) continue;
			
			SQL.append(entry.getKey().getColumn()).append("=?,");
			params.add(entry.getValue());
		}
		
		SQL.append("modifier=?, last_modified=? WHERE page_id=?");
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
	public PageInfo query(InfoId<?> id) {
		
		String SQL = "SELECT * FROM gp_pages WHERE page_id = ?";
		
		Object[] params = new Object[]{
				id.getId()
		};
		
		JdbcTemplate jtemplate = getJdbcTemplate(JdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		
		List<PageInfo> pinfos = jtemplate.query(SQL, params, PAGE_MAPPER);
		
		return CollectionUtils.isEmpty(pinfos)? null : pinfos.get(0);
	}

	
	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
