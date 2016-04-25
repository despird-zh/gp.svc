package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.dao.MeasureDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.info.MeasureInfo;

@Component("measureDAO")
public class MeasureDAOImpl extends DAOSupport implements MeasureDAO{

	static Logger LOGGER = LoggerFactory.getLogger(MeasureDAOImpl.class);
	
	SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	String MYSQL_DT_FMT = "%Y-%m-%d %H:%i:%s";
	
	@Autowired
	public MeasureDAOImpl(DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public MeasureInfo queryObjectByTraceId(InfoId<?> traceid, String measureType, FlatColLocator... columns) {
		
		StringBuffer SQL = new StringBuffer("SELECT measure_id, trace_src_id, measure_time, measure_type,");
		if(!ArrayUtils.isEmpty(columns)){
			for(FlatColLocator fcl:columns){
				SQL.append(fcl.getColumn()).append(",");				
			}
		}
		SQL.append("modifier,last_modified FROM gp_measures ");
		SQL.append("WHERE trace_src_id = ? AND measure_type = ?");
		
		Object[] params = new Object[]{
				traceid.getId(),measureType				
		};
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / PARAMS : " + ArrayUtils.toString(params));
		}
		RowMapper<MeasureInfo> rowmapper = getRowMapper(columns);
		
		List<MeasureInfo> minfos = jtemplate.query(SQL.toString(),params, rowmapper);
		
		return CollectionUtils.isEmpty(minfos)? null : minfos.get(0);

	}

	@Override
	public List<MeasureInfo> queryListByTraceId(InfoId<?> traceid, String measureType, FlatColLocator... columns) {
		
		StringBuffer SQL = new StringBuffer("SELECT measure_id, trace_src_id, measure_time, measure_type,");
		if(!ArrayUtils.isEmpty(columns)){
			for(FlatColLocator fcl:columns){
				SQL.append(fcl.getColumn()).append(",");				
			}
		}
		SQL.append("modifier,last_modified FROM gp_measures ");
		SQL.append("WHERE trace_src_id = ? AND measure_type = ?");
		
		Object[] params = new Object[]{
				traceid.getId(),measureType				
		};
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / PARAMS : " + ArrayUtils.toString(params));
		}
		RowMapper<MeasureInfo> rowmapper = getRowMapper(columns);
		
		List<MeasureInfo> minfos = jtemplate.query(SQL.toString(),params, rowmapper);
		
		return minfos;
	}

	@Override
	public List<MeasureInfo> queryListBefore(InfoId<?> traceid, String measureType, Date before,
			FlatColLocator... columns) {
		
		StringBuffer SQL = new StringBuffer("SELECT measure_id, trace_src_id, measure_time, measure_type,");
		if(!ArrayUtils.isEmpty(columns)){
			for(FlatColLocator fcl:columns){
				SQL.append(fcl.getColumn()).append(",");				
			}
		}
		SQL.append("modifier,last_modified FROM gp_measures ");
		SQL.append("WHERE trace_src_id = ? AND measure_type = ? AND measure_time < ? ");
		
		Object[] params = new Object[]{
				traceid.getId(),measureType, new java.sql.Timestamp(before.getTime())		
		};
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / PARAMS : " + ArrayUtils.toString(params));
		}
		RowMapper<MeasureInfo> rowmapper = getRowMapper(columns);
		
		List<MeasureInfo> minfos = jtemplate.query(SQL.toString(),params, rowmapper);
		
		return minfos;
	}

	@Override
	public List<MeasureInfo> queryListAfter(InfoId<?> traceid, String measureType, Date after,
			FlatColLocator... columns) {
		StringBuffer SQL = new StringBuffer("SELECT measure_id, trace_src_id, measure_time, measure_type,");
		if(!ArrayUtils.isEmpty(columns)){
			for(FlatColLocator fcl:columns){
				SQL.append(fcl.getColumn()).append(",");				
			}
		}
		SQL.append("modifier,last_modified FROM gp_measures ");
		SQL.append("WHERE trace_src_id = ? AND measure_type = ? AND measure_time > STR_TO_DATE( ? ,'").append(MYSQL_DT_FMT).append("' )");
		
		Object[] params = new Object[]{
				traceid.getId(),measureType, DATE_FMT.format(after)	
		};
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / PARAMS : " + ArrayUtils.toString(params));
		}
		RowMapper<MeasureInfo> rowmapper = getRowMapper(columns);
		
		List<MeasureInfo> minfos = jtemplate.query(SQL.toString(),params, rowmapper);
		
		return minfos;
	}

	@Override
	public List<MeasureInfo> queryListRange(InfoId<?> traceid, String measureType, Date before, Date after,
			FlatColLocator... columns) {
		StringBuffer SQL = new StringBuffer("SELECT measure_id, trace_src_id, measure_time, measure_type,");
		if(!ArrayUtils.isEmpty(columns)){
			for(FlatColLocator fcl:columns){
				SQL.append(fcl.getColumn()).append(",");				
			}
		}
		SQL.append("modifier,last_modified FROM gp_measures ");
		SQL.append("WHERE trace_src_id = ? AND measure_type = ? AND measure_time > ? AND measure_time <= ?");
		
		Object[] params = new Object[]{
				traceid.getId(),measureType, new java.sql.Timestamp(after.getTime()), new java.sql.Timestamp(before.getTime())		
		};
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / PARAMS : " + ArrayUtils.toString(params));
		}
		RowMapper<MeasureInfo> rowmapper = getRowMapper(columns);
		
		List<MeasureInfo> minfos = jtemplate.query(SQL.toString(),params, rowmapper);
		
		return minfos;
	}

	public RowMapper<MeasureInfo> getRowMapper(final FlatColLocator... columns) {
		
		return new RowMapper<MeasureInfo>(){
			
			private FlatColLocator[] _columns = columns;
			
			@Override
			public MeasureInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				MeasureInfo measure = new MeasureInfo();
				InfoId<Long> mid = IdKey.MEASURE.getInfoId(rs.getLong("measure_id"));
				measure.setInfoId(mid);
				
				if(!ArrayUtils.isEmpty(_columns)){
					for(FlatColLocator fcl : _columns){
						measure.putColValue(fcl, rs.getString(fcl.getColumn()));			
					}
				}
				
				measure.setMeasureTime(rs.getTimestamp("measure_time"));
				measure.setMeasureType(rs.getString("measure_type"));
				measure.setModifier(rs.getString("modifier"));
				measure.setModifyDate(rs.getTimestamp("last_modified"));
				return measure;
			}
			
		};
	}
}
