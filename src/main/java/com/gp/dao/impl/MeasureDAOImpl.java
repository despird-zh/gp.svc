package com.gp.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.gp.dao.MeasureDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.MeasureInfo;

@Component("measureDAO")
public class MeasureDAOImpl extends DAOSupport implements MeasureDAO{

	@Autowired
	public MeasureDAOImpl(DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public MeasureInfo queryObjectByTraceId(Long traceid, FlatColLocator... columns) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MeasureInfo> queryListByTraceId(Long traceid, FlatColLocator... columns) {
		// TODO Auto-generated method stub
		return null;
	}

}
