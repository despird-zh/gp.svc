package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.dao.MeasureDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.info.MeasureInfo;

@Component("measureDAO")
public class MeasureDAOImpl extends DAOSupport implements MeasureDAO{

	static Logger LOGGER = LoggerFactory.getLogger(MeasureDAOImpl.class);
	
	//SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	//String MYSQL_DT_FMT = "%Y-%m-%d %H:%i:%s";
	
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
				traceid.getId(),measureType, before		
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
		SQL.append("WHERE trace_src_id = ? AND measure_type = ? AND measure_time > ?");
		
		Object[] params = new Object[]{
				traceid.getId(),measureType, after
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
				traceid.getId(),measureType, after, before	
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
	public int create(MeasureInfo info) {
		
		StringBuffer SQL = new StringBuffer("INSERT into gp_measures (measure_id, measure_time, measure_type, trace_src_id,");
		StringBuffer COLS = new StringBuffer();
		StringBuffer VALS = new StringBuffer(")VALUES(?,?,?,?,");
		
		ArrayList<Object> params = new ArrayList<Object>();
		// prepare 4 params
		params.add(info.getInfoId().getId());
		params.add(info.getMeasureTime());
		params.add(info.getMeasureType());
		params.add(info.getTraceSourceId());
		
		for(Map.Entry<FlatColLocator, String> entry : info.getFlatColMap().entrySet()){
			
			COLS.append(entry.getKey().getColumn()).append(",");
			VALS.append("?,");
			params.add(entry.getValue());
		}
		
		SQL.append(COLS)
			.append("modifier, last_modified )")
			.append(VALS)
			.append("?,? )");
		
		params.add(info.getModifier());
		params.add(info.getModifyDate());
		
		Object[] paramary = params.toArray();
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / PARAMS : " + ArrayUtils.toString(paramary));
		}
		
		int cnt  = jtemplate.update(SQL.toString(), paramary);
		return cnt;
	}

	@Override
	public int delete(InfoId<?> id) {
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_measures ")
			.append("where measure_id = ? ");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			id.getId()
		};
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(), params);
		return rtv;
	}

	@Override
	public int update(MeasureInfo info) {
		
		StringBuffer SQL = new StringBuffer("UPDATE gp_measures SET measure_time = ?, measure_type = ?, trace_src_id = ?,");

		
		ArrayList<Object> params = new ArrayList<Object>();
		// prepare 3 params
		params.add(info.getMeasureTime());
		params.add(info.getMeasureType());
		params.add(info.getTraceSourceId());
		
		for(Map.Entry<FlatColLocator, String> entry : info.getFlatColMap().entrySet()){
			
			SQL.append(entry.getKey().getColumn()).append(" = ?,");
			params.add(entry.getValue());
		}
		
		SQL.append("modifier = ?, last_modified = ? WHERE measure_id= ?");
		
		params.add(info.getModifier());
		params.add(info.getModifyDate());
		params.add(info.getInfoId().getId());
		
		Object[] paramary = params.toArray();
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / PARAMS : " + ArrayUtils.toString(paramary));
		}
		
		int cnt  = jtemplate.update(SQL.toString(), paramary);
		return cnt;
	}

	@Override
	public MeasureInfo query(InfoId<?> id) {
		
		String SQL = "SELECT * FROM gp_measures WHERE measure_id = ?";
		
		Object[] params = new Object[]{id.getId()};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / PARAMS : " + ArrayUtils.toString(params));
		}
		RowMapper<MeasureInfo> rowmapper = getRowMapper();
		
		List<MeasureInfo> minfos = jtemplate.query(SQL.toString(),params, rowmapper);
		
		return CollectionUtils.isEmpty(minfos) ? null : minfos.get(0);
	}

	@Override
	public RowMapper<MeasureInfo> getRowMapper() {
		
		return getRowMapper(new FlatColLocator[0]);
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
