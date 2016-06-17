package com.gp.dao;

import org.springframework.jdbc.core.RowMapper;

import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;

/**
 * Base DAO definition class, provides CRUD etc. methods
 * It supports two generic types<T, K>, T is row wrapper class, K is the key wrapper
 * 
 * @author gary diao
 * @version 0.1 2015-12-12
 * 
 **/
public interface BaseDAO <T> {
	
	/**
	 * Create record with row bean information.
	 * 
	 * @param info the row bean
	 * @return K the row id
	 **/
	public int create(T info);
	
	/**
	 * Delete the row by id
	 * 
	 * @param id the row id
	 * @return int the row count deleted.
	 **/
	public int delete(InfoId<?> id);
	
	/**
	 * Update the specified row with row bean
	 * 
	 * @param id the row id
	 * @param info the information of row
	 * @param excludeCols the columns excluded from update
	 * 
	 * @return int the row count of updated.
	 **/
	public int update(T info, FlatColLocator ... excludeCols);

	/**
	 * Query row by id
	 * 
	 * @param id the row id
	 * @return T the row bean. 
	 **/
	public T query(InfoId<?> id);

	/**
	 * Get the row wrapper.
	 **/
	public RowMapper<T> getRowMapper();
}
