package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.dao.PageDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.FlatColumn;
import com.gp.info.InfoId;
import com.gp.info.MeasureInfo;
import com.gp.info.PageInfo;

@Component("pagedao")
public class PageDAOImpl extends DAOSupport implements PageDAO {

	Logger LOGGER = LoggerFactory.getLogger(PageDAOImpl.class);
	
	@Autowired
	public PageDAOImpl(DataSource dataSource) {
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
	public int update(PageInfo info) {
		
		StringBuffer SQL = new StringBuffer("UPDATE gp_pages (page_name=?,");
		SQL.append("module=?,descr=?,page_abbr=?,");

		List<Object> params = new ArrayList<Object>();
		params.add(info.getPageName());
		params.add(info.getModule());
		params.add(info.getDescription());
		params.add(info.getPageAbbr());
		
		for(Map.Entry<FlatColLocator, String> entry: info.getActionMap().entrySet()){
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
	public RowMapper<PageInfo> getRowMapper() {
		
		return PAGE_MAPPER;
	}
	
	public static RowMapper<PageInfo> PAGE_MAPPER = new RowMapper<PageInfo>(){

		@Override
		public PageInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			PageInfo page = new PageInfo();
			
			InfoId<Integer> mid = IdKey.PAGE.getInfoId(rs.getInt("page_id"));
			page.setInfoId(mid);
			
			page.setDescription(rs.getString("descr"));
			page.setModule(rs.getString("module"));
			page.setPageName(rs.getString("page_name"));
			page.setPageAbbr(rs.getString("page_abbr"));
			
			for(int i = 1; i <= COLUMN_COUNT ; i++){
				String val = rs.getString("act_abbr_"+i);
				if(StringUtils.isNotBlank(val)){
					FlatColumn col = new FlatColumn("act_abbr_", i);
					page.putColValue(col, val);
				}
				
			}
			
			page.setModifier(rs.getString("modifier"));
			page.setModifyDate(rs.getTimestamp("last_modified"));
			return page;
		}
		
	};

	
	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
