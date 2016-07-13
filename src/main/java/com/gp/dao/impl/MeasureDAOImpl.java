package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.FlatColumns;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.common.IdKey;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.MeasureDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.info.MeasureInfo;

@Component("measureDAO")
public class MeasureDAOImpl extends DAOSupport implements MeasureDAO{

	static Logger LOGGER = LoggerFactory.getLogger(MeasureDAOImpl.class);

	@Autowired
	public MeasureDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public MeasureInfo queryLatest(InfoId<?> traceid, String measureType, FlatColLocator... columns) {
		
		String SQL_LATEST_ID = "SELECT max(measure_id) FROM gp_measures WHERE trace_src_id = ? AND measure_type = ?";
		StringBuffer SQL = new StringBuffer("SELECT measure_id, trace_src_id, measure_time, measure_type,");
		if(!ArrayUtils.isEmpty(columns)){
			for(FlatColLocator fcl:columns){
				SQL.append(fcl.getColumn()).append(",");				
			}
		}
		SQL.append("modifier,last_modified FROM gp_measures ");
		SQL.append("WHERE measure_id = ?");
		
		Object[] params = new Object[]{
				traceid.getId(),measureType				
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL_LATEST_ID + " / PARAMS : " + ArrayUtils.toString(params));
		}
		List<Long> ids = jtemplate.queryForList(SQL_LATEST_ID,params, Long.class);
		if(CollectionUtils.isEmpty(ids)){
			// no record return directly
			return null;
		}
		
		RowMapper<MeasureInfo> rowmapper = getRowMapper(columns);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / PARAMS : " + ids.get(0));
		}
		// search according the id
		List<MeasureInfo> minfos = jtemplate.query(SQL.toString(),new Object[]{ids.get(0)}, rowmapper);
		
		return CollectionUtils.isEmpty(minfos)? null : minfos.get(0);

	}

	@Override
	public List<MeasureInfo> query(InfoId<?> traceid, String measureType, FlatColLocator... columns) {
		
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
	public List<MeasureInfo> queryBefore(InfoId<?> traceid, String measureType, Date before,
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
	public List<MeasureInfo> queryAfter(InfoId<?> traceid, String measureType, Date after,
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
	public List<MeasureInfo> queryRange(InfoId<?> traceid, String measureType, Date before, Date after,
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
	public int update(MeasureInfo info, FilterMode mode,FlatColLocator ...exclcols) {
		Set<String> colset = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer("UPDATE gp_measures SET ");
		if(columnCheck(mode, colset, "measure_time")){
			SQL.append("measure_time = ?, ");
			params.add(info.getMeasureTime());
		}
		if(columnCheck(mode, colset, "measure_type")){
			SQL.append("measure_type = ?, ");
			params.add(info.getMeasureType());
		}
		if(columnCheck(mode, colset, "trace_src_id")){
			SQL.append("trace_src_id = ?,");
			params.add(info.getTraceSourceId());
		}
		
		for(Map.Entry<FlatColLocator, String> entry : info.getFlatColMap().entrySet()){
			if(!columnCheck(mode, colset, entry.getKey().getColumn())) continue;
			
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

	@Override
	public int updateByTraceId(InfoId<?> traceid, String measureType, FlatColLocator flatcol, String value) {
		
		StringBuffer SQL = new StringBuffer("UPDATE gp_measures SET ");
		
		ArrayList<Object> params = new ArrayList<Object>();

		SQL.append(flatcol.getColumn()).append(" = ?");
		params.add(value);
		
		SQL.append("WHERE trace_src_id = ? AND measure_type = ? ");
		
		params.add(traceid.getId());
		params.add(measureType);

		Object[] paramary = params.toArray();
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / PARAMS : " + ArrayUtils.toString(paramary));
		}
		
		int cnt  = jtemplate.update(SQL.toString(), paramary);
		return cnt;
	}

	@Override
	public int updateByTraceId(InfoId<?> traceid, String measureType, Map<FlatColLocator, String> colmap) {
		
		StringBuffer SQL = new StringBuffer("UPDATE gp_measures SET ");
		
		ArrayList<Object> params = new ArrayList<Object>();

		for(Map.Entry<FlatColLocator, String> entry : colmap.entrySet()){
			
			SQL.append(entry.getKey().getColumn()).append(" = ?,");
			params.add(entry.getValue());
		}
		
		SQL.append("WHERE trace_src_id = ? AND measure_type = ? ");
		
		params.add(traceid.getId());
		params.add(measureType);

		Object[] paramary = params.toArray();
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / PARAMS : " + ArrayUtils.toString(paramary));
		}
		
		int cnt  = jtemplate.update(SQL.toString(), paramary);
		return cnt;
	}
	
}
