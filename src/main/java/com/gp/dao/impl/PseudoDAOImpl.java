package com.gp.dao.impl;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.gp.dao.PseudoDAO;

@Component("pseudoDAO")
public class PseudoDAOImpl extends DAOSupport implements PseudoDAO{

	NamedParameterJdbcTemplate nameJdbcTemplate = null;
	
	@Autowired
	public PseudoDAOImpl(DataSource dataSource) {
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
	
}
