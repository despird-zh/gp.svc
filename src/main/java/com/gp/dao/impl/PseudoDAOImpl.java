package com.gp.dao.impl;

import java.util.Date;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.gp.config.ServiceConfigurer;
import com.gp.dao.PseudoDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;

@Component("pseudoDAO")
public class PseudoDAOImpl extends DAOSupport implements PseudoDAO{

	static Logger LOGGER = LoggerFactory.getLogger(PseudoDAOImpl.class);
	
	NamedParameterJdbcTemplate nameJdbcTemplate = null;
	
	@Autowired
	public PseudoDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.nameJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	/**
	 * try to hold two templates to improve performance. 
	 **/
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getJdbcTemplate(Class<T> clazz){
    	
    	if (NamedParameterJdbcTemplate.class.equals(clazz)){
    		
    		return (T) nameJdbcTemplate;
    	}else if (JdbcTemplate.class.equals(clazz)){
    		
    		return (T)this.jdbcTemplate;
    	}else{
    		
    		return null;
    	}
    }

	@Override
	public Integer update(InfoId<?> id, Map<FlatColLocator, Object> fields) {
		
		StringBuffer SQL = new StringBuffer("UPDATE ");
		Object[] params = new Object[fields.size() + 2]; // append one last_modified and id column
		SQL.append(id.getIdKey()).append(" SET ");
		int pos = 0;
		for(FlatColLocator col: fields.keySet()){
			SQL.append(col.getColumn()).append(" = ?,");
			params[pos] = fields.get(col);
			pos ++;
		}
		
		SQL.append("last_modified").append(" = ? ");
		params[pos] = new Date();
		
		SQL.append("WHERE ").append(id.getIdColumn()).append(" = ?");
		params[pos + 1] = id.getId();
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL.toString(), ArrayUtils.toString(params));
		}
		
		return jtemplate.update(SQL.toString(), params);
	}

	@Override
	public Integer update(InfoId<?> id, FlatColLocator col, Object val) {
		
		StringBuffer SQL = new StringBuffer("UPDATE ");
		Object[] params = new Object[3]; // append one last_modified and id column
		SQL.append(id.getIdKey()).append(" SET ");

		SQL.append(col.getColumn()).append(" = ?,");
		params[0] = val;
		
		SQL.append("last_modified").append(" = ? ");
		params[1] = new Date();
		
		SQL.append("WHERE ").append(id.getIdColumn()).append(" = ?");
		params[2] = id.getId();
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL.toString(), ArrayUtils.toString(params));
		}
		return jtemplate.update(SQL.toString(), params);
	}

	@Override
	public Integer update(InfoId<?> id, FlatColLocator[] cols, Object[] vals) {
		
		if(cols.length != vals.length) 
			throw new UnsupportedOperationException("cols and vals not same length");
		
		StringBuffer SQL = new StringBuffer("UPDATE ");
		Object[] params = new Object[cols.length + 2]; // append one last_modified and id column
		SQL.append(id.getIdKey()).append(" SET ");
		int pos = 0;
		for(FlatColLocator col: cols){
			SQL.append(col.getColumn()).append(" = ?,");
			params[pos] = vals[pos];
			pos ++;
		}
		
		SQL.append("last_modified").append(" = ? ");
		params[pos] = new Date();
		
		SQL.append("WHERE ").append(id.getIdColumn()).append(" = ?");
		params[pos + 1] = id.getId();
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL.toString(), ArrayUtils.toString(params));
		}
		
		return jtemplate.update(SQL.toString(), params);
	}
	
}
