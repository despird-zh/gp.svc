package com.gp.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import com.gp.config.ServiceConfigurer;
import com.gp.dao.PseudoDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;

@Component
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
	public SimpleJdbcCall getJdbcCall(String procedureName) {

		return new SimpleJdbcCall((JdbcTemplate)this.jdbcTemplate).withProcedureName(procedureName);
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

	@Override
	public <T> T query(InfoId<?> id, FlatColLocator col, Class<T> clazz) {
		
		StringBuffer SQL = new StringBuffer("SELECT ");
		SQL.append(col.getColumn())
			.append(" FROM ")
			.append(id.getIdKey())
			.append(" WHERE ").append(id.getIdColumn()).append("=?");
		
		Object[] params = new Object[]{
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL.toString(), ArrayUtils.toString(params));
		}
		
		List<T> ol = jtemplate.queryForList(SQL.toString(), params, clazz);
		return CollectionUtils.isEmpty(ol) ? null : ol.get(0);
	}

	@Override
	public Map<String, Object> query(InfoId<?> id, FlatColLocator... cols) {
		
		StringBuffer SQL = new StringBuffer("SELECT ");
		for(FlatColLocator col : cols){
			SQL.append(col.getColumn()).append(",");
		}
		if(SQL.lastIndexOf(",") == SQL.length() -1)
			SQL.deleteCharAt(SQL.length() -1);
		
		SQL.append(" FROM ")
			.append(id.getIdKey())
			.append(" WHERE ").append(id.getIdColumn()).append(" = ?");
		
		Object[] params = new Object[]{
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL.toString(), ArrayUtils.toString(params));
		}
		
		List<Map<String, Object>> mlist = jtemplate.query(SQL.toString(), params, new ColumnMapRowMapper());
		
		if( CollectionUtils.isEmpty(mlist))
			return null ;
		else {
			return mlist.get(0);
		}
		
	}

}
