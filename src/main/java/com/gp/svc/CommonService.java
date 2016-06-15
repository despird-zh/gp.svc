package com.gp.svc;

import java.util.Map;

import com.gp.exception.ServiceException;
import com.gp.info.FlatColLocator;
import com.gp.info.Identifier;
import com.gp.info.InfoId;

/**
 * CommonService provides method for general purpose i.e. it make methods of pseudodao exposed. 
 * e.g generateId 
 **/
public interface CommonService {

	/**
	 * Generate new Id for record
	 * 
	 * @param idkey the key of identifier
	 * @param keytype the type of expected InfoId
	 * 
	 * @return the expected id
	 **/
	public <T> InfoId<T> generateId( Identifier idkey,Class<T> keytype) throws ServiceException;

	/**
	 * Generate new Id for record
	 * 
	 * @param modifier the account of principal who request id
	 * @param idkey the key of identifier
	 * @param keytype the type of expected InfoId
	 * 
	 * @return the expected id
	 **/
	public <T> InfoId<T> generateId(String modifier, Identifier idkey, Class<T> type) throws ServiceException;

	/**
	 * Update the table in flat column mode
	 * 
	 * @param id the id of record
	 * @param fields the field map which holds the column-value pairs
	 * 
	 * @return Integer the row count updated 
	 **/
	public Integer update(InfoId<?> id, Map<FlatColLocator, Object> fields)throws ServiceException;
	
	/**
	 * Update the table in flat column mode
	 * 
	 * @param id the id of record
	 * @param col the column to be updated
	 * @param val the value of column
	 * 
	 * @return Integer the row count updated 
	 **/
	public Integer update(InfoId<?> id, FlatColLocator col, Object val)throws ServiceException;
	
	/**
	 * Update the table in flat column mode
	 * 
	 * @param id the id of record
	 * @param col the column array to be updated
	 * @param val the values of column array
	 * 
	 * @return Integer the row count updated 
	 **/
	public Integer update(InfoId<?> id, FlatColLocator[] col, Object[] val)throws ServiceException;
	
	/**
	 * Query table in flat mode
	 * 
	 * @param id the id of row
	 * @param col the flat column locator 
	 * @return Object the column value
	 **/
	public <T> T query(InfoId<?> id, FlatColLocator col, Class<T> clazz)throws ServiceException;
	
	/**
	 * Query table in flat mode
	 * 
	 * @param id the id of row
	 * @param col the flat column locator 
	 * @return Map<String, Object> the map key is FlatColLocator.getColumn() i.e. column name
	 **/
	public Map<String, Object> query(InfoId<?> id, FlatColLocator[] cols)throws ServiceException;
}
