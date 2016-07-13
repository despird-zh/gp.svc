package com.gp.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;

import com.gp.common.FlatColumns.FilterMode;
import com.gp.common.GeneralConfig;
import com.gp.common.SystemOptions;
import com.gp.pagination.PageQuery;

public abstract class DAOSupport{

	static Logger LOGGER = LoggerFactory.getLogger(DAOSupport.class);
	
	public static enum DatabaseProduct{		
		Oracle,
		MySql;
	}
	
	/**
	 * The data source object
	 **/
    private DataSource dataSource;
    
    private String databaseProduct = null;
    
    /**
     * Initial the jdbc template with data source. 
     **/
    protected abstract void initialJdbcTemplate(DataSource dataSource);
    
    protected Object jdbcTemplate = null;
	
    /**
     * Get the jdbc template, it could be {@link NamedParameterJdbcTemplate} or {@link JdbcTemplate}
     * @param <T>
     **/
    @SuppressWarnings("unchecked")
	public <T> T getJdbcTemplate(Class<T> clazz){
    	
    	if(clazz.isInstance(jdbcTemplate))
    		return (T)jdbcTemplate;
    	else if (NamedParameterJdbcTemplate.class.equals(clazz)){
    		
    		return (T) new NamedParameterJdbcTemplate(dataSource);
    	}else if (JdbcTemplate.class.equals(clazz)){
    		
    		return (T) new JdbcTemplate(dataSource);
    	}else{
    		
    		return null;
    	}
    }
    
    /**
     * Get data source object 
     **/
    public DataSource getDataSource() {
        return dataSource;
    }

    public String getDatabaseProduct(){
    	
    	return this.databaseProduct;
    }
    
    /**
     * Set the data source object 
     **/
    public void setDataSource(DataSource dataSource){
        
    	this.dataSource = dataSource;    	
        initialJdbcTemplate(dataSource);
        
        try {		
        	databaseProduct = (String)JdbcUtils.extractDatabaseMetaData(dataSource,"getDatabaseProductName");
        } catch (MetaDataAccessException e) {
        	// ignore setting
        	LOGGER.error("fail to fetch meta data : product name",e);        	
        }
        
        if(null != jdbcTemplate && jdbcTemplate instanceof JdbcTemplate){
        	String value = GeneralConfig.getString(SystemOptions.QUERY_MAX_ROWS,"2000");
        	int maxrows= Integer.valueOf(value);
        	((JdbcTemplate)jdbcTemplate).setMaxRows(maxrows);
        }
    }

    /**
     * Get the connection object 
     **/
    public Connection getConnection() {
        Connection conn = null;
        conn = DataSourceUtils.getConnection(dataSource);
        return conn;
    }
    
    /**
     * release the connection object 
     **/
    public void releaseConnection(Connection conn){
    	
    	DataSourceUtils.releaseConnection(conn,dataSource);
    }
        
    /**
     * decorate SQL to fetch specified page rows
     * @param sql SQL script
     * @param pagequery the page query information 
     **/
    public String getPageQuerySql(String sql, PageQuery pagequery){
    	
    	if(DatabaseProduct.MySql.name().equalsIgnoreCase(databaseProduct)){
    		
    		String newSql = sql + " LIMIT " + (pagequery.getPageNumber()-1) * pagequery.getPageSize() + " , " + pagequery.getPageSize() ;
    		return newSql;
    	
    	}else if(DatabaseProduct.Oracle.name().equalsIgnoreCase(databaseProduct)){
    		
    		String newSql = null;
    		if(pagequery.getPageNumber() == 1){
    			newSql = "SELECT ROWNUM ROW_NUM, SUBQ.* " 
    			+ " FROM "
    			+ " ( " +sql + " ) SUBQ "
    			+ " WHERE ROW_NUM <= " + pagequery.getPageSize();
    		}else{
    			newSql = "SELECT * FROM ( "
    			+ " SELECT ROWNUM ROW_NUM, SUBQ.* "
    			+ " FROM "
    			+ " ( " +sql + " ) SUBQ "
    			+ " WHERE ROW_NUM <= " + pagequery.getPageNumber() * pagequery.getPageSize() + ")"
    			+ "    WHERE ROW_NUM > " + (pagequery.getPageNumber()-1) * pagequery.getPageSize() ;
    			
    		}
    		
    		return newSql;
    	}
    	else{
    		
    		throw new UnsupportedOperationException("not supported paging feature");
    	}
    }

    /**
     * decorate SQL to fetch specified page rows
     * @param sql SQL script
     * @param start the start position
     * @param length the length of query rows
     **/
    public String getPageQuerySql(String sql, int start, int length){
    	
    	if(DatabaseProduct.MySql.name().equalsIgnoreCase(databaseProduct)){
    		
    		String newSql = sql + " LIMIT " + start + " , " + length ;
    		return newSql;
    	
    	}else if(DatabaseProduct.Oracle.name().equalsIgnoreCase(databaseProduct)){
    		
    		String newSql = null;
    		if(start == 0){
    			newSql = "SELECT ROWNUM ROW_NUM, SUBQ.* " 
    			+ " FROM "
    			+ " ( " +sql + " ) SUBQ "
    			+ " WHERE ROW_NUM <= " + length;
    		}else{
    			newSql = "SELECT * FROM ( "
    			+ " SELECT ROWNUM ROW_NUM, SUBQ.* "
    			+ " FROM "
    			+ " ( " +sql + " ) SUBQ "
    			+ " WHERE ROW_NUM <= " + (start + length) + ")"
    			+ "    WHERE ROW_NUM > " + start ;
    			
    		}
    		
    		return newSql;
    	}
    	else{
    		
    		throw new UnsupportedOperationException("not supported paging feature");
    	}
    }
    
	/**
	 * query row count of specified sql scripts
	 * usually it's called during pagination processing.
	 * 
	 * @param countsql the counting sql 
	 * 
	 **/
	public Integer queryRowCount(String countsql){
		
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + countsql);
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		
		Integer count = jtemplate.queryForObject(countsql, Integer.class);
		
		return count ;
	}

	/**
	 * query row count of specified sql scripts
	 * usually it's called during pagination processing.
	 * 
	 * @param countsql the counting sql 
	 * 
	 **/
	public Integer queryRowCount(NamedParameterJdbcTemplate template, String countsql, Map<String, Object> paramap) {
		
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + countsql.toString());
		}

		Integer count = template.queryForObject(countsql, paramap, Integer.class);
		
		return count ;
	}

	/**
	 * query row count of specified sql scripts
	 * usually it's called during pagination processing.
	 * 
	 * @param countsql the counting sql 
	 * 
	 **/
	public Integer queryRowCount(JdbcTemplate template, String countsql, Object[] paras) {
		
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + countsql.toString());
		}

		Integer count = template.queryForObject(countsql, paras, Integer.class);
		
		return count ;
	}
	
    /**
     * Convert the list of string into IN 
     * 
     * @param idkey indicate the table  
     **/
	public static String getInClause(Collection<String> list){
		
		if(!CollectionUtils.isEmpty(list)){
			
			StringBuffer sbuf = new StringBuffer();
			
			for(String item: list){
				
				sbuf.append("'").append(item).append("',");
			}
			sbuf.delete(sbuf.length()-1, sbuf.length());
			
			return sbuf.toString();
		}else{
			
			return null;
		}
	}

	/**
	 * Check if result has specified column 
	 * @param rs The result set object
	 * @param columnName the target column name to be checked
	 * 
	 * @return boolean exist return true, otherwise return false
	 **/
	public static boolean hasColInResultSet(ResultSet resultset, String columnName) throws SQLException {
	    ResultSetMetaData rs_metadata = resultset.getMetaData();
	    int columns = rs_metadata.getColumnCount();
	    for (int x = 1; x <= columns; x++) {
	        if (StringUtils.equalsIgnoreCase(columnName, rs_metadata.getColumnName(x))) {
	            return true;
	        }
	    }
	    return false;
	}
	
	/**
	 * check if the column need update.
	 * @param mode the file mode include or exclude
	 * @param colset the set of columns
	 * @param column the column name  
	 **/
	protected boolean columnCheck(FilterMode mode, Set<String> colset, String column){
		
		return FilterMode.EXCLUDE == mode && !colset.contains("workgroup_id") || 
				   FilterMode.INCLUDE == mode && colset.contains("workgroup_id")||
				   FilterMode.NONE == mode;
	}
}
