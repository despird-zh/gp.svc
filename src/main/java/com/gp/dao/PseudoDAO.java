package com.gp.dao;

import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.pagination.PageQuery;

/**
 * The interface wrap the common function might be used in Service level.
 * Here not expose these method to DAO or Service interface.
 * 
 * @author gary 
 * @version 0.1 2016-3-4
 **/
public interface PseudoDAO {

	public <T> T getJdbcTemplate(Class<T> clazz);
	
	public SimpleJdbcCall getJdbcCall(String procedureName);
	
	/**
	 * convert the sql into script with pagination setting
	 * @param sql the original sql script
	 * @param pagequery the page query parameter 
	 **/
	public String getPageQuerySql(String sql, PageQuery pagequery);
	
	/**
	 * convert the sql into script with pagination setting
	 * @param sql the original sql script
	 * @param start the start position 
	 * @param length the length of query rows
	 **/
	public String getPageQuerySql(String sql, int start, int length);
	
	/**
	 * use default {@link JdbcTemplate} to query the row count
	 * @param countsql the count sql script
	 **/
	public Integer queryRowCount(String countsql);
	
	/**
	 * use {@link NamedParameterJdbcTemplate} and parameter map query row count
	 * @param template the template object
	 * @param sql the original sql
	 * @param paramap the parameter map
	 **/
	public Integer queryRowCount(NamedParameterJdbcTemplate template, String sql, Map<String, Object> paramap);
	
	/**
	 * use {@link JdbcTemplate} and parameter list query row count
	 * @param template the template object
	 * @param sql the original sql
	 * @param params the parameter objects array
	 **/
	public Integer queryRowCount(JdbcTemplate template, String sql, Object[] params);

	/**
	 * Update the table in flat column mode
	 * 
	 * @param id the id of record
	 * @param fields the field map which holds the column-value pairs
	 * 
	 * @return Integer the row count updated 
	 **/
	public Integer update(InfoId<?> id, Map<FlatColLocator, Object> fields);
	
	/**
	 * Update the table in flat column mode
	 * 
	 * @param id the id of record
	 * @param col the column to be updated
	 * @param val the value of column
	 * 
	 * @return Integer the row count updated 
	 **/
	public Integer update(InfoId<?> id, FlatColLocator col, Object val);
	
	/**
	 * Update the table in flat column mode
	 * 
	 * @param id the id of record
	 * @param col the column array to be updated
	 * @param val the values of column array
	 * 
	 * @return Integer the row count updated 
	 **/
	public Integer update(InfoId<?> id, FlatColLocator[] col, Object[] val);
	
	/**
	 * Query table in flat mode
	 * 
	 * @param id the id of row
	 * @param col the flat column locator 
	 * @return Object the column value
	 **/
	public Object query(InfoId<?> id, FlatColLocator col);
	
	/**
	 * Query table in flat mode
	 * 
	 * @param id the id of row
	 * @param col the flat column locator 
	 * @return Map<String, Object> the map key is FlatColLocator.getColumn() i.e. column name
	 **/
	public Map<String, Object> query(InfoId<?> id, FlatColLocator[] cols);
}
