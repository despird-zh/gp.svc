package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.config.ServiceConfigurator;
import com.gp.dao.WorkgroupMirrorDAO;
import com.gp.info.InfoId;
import com.gp.info.WorkgroupMirrorInfo;

@Component("workgroupMirrorDAO")
public class WorkgroupMirrorDAOImpl extends DAOSupport implements WorkgroupMirrorDAO{

	static Logger LOGGER = LoggerFactory.getLogger(WorkgroupMirrorDAOImpl.class);

	@Autowired
	public WorkgroupMirrorDAOImpl(@Qualifier(ServiceConfigurator.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create(WorkgroupMirrorInfo info) {
		StringBuffer SQL = new StringBuffer();
		
		SQL.append("insert into gp_workgroup_mirror (")
			.append("source_id,workgroup_id,mirror_id,")
			.append("mirror_state,mirror_owm,last_sync_time,")
			.append("modifier, last_modified")
			.append(")values(")
			.append("?,?,?,")
			.append("?,?,?,")
			.append("?,?)");

		InfoId<Long> key = info.getInfoId();
		
		Object[] params = new Object[]{
			info.getSourceId(),info.getWorkgroupId(),key.getId(),
			info.getState(),info.getOwm(),info.getLastSyncDate(),
			info.getModifier(),info.getModifyDate()
		};

		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		return jtemplate.update(SQL.toString(),params);
	}

	@Override
	public int delete(InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_workgroup_mirror ")
			.append("where mirror_id = ? and source_id = ? ");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			id.getId()
		};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = -1;

			rtv = jtemplate.update(SQL.toString(), params);

		return rtv;
	}

	@Override
	public int update(WorkgroupMirrorInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_workgroup_mirror set ")
		.append("workgroup_id = ?,source_id = ? ,")
		.append("mirror_state = ?,mirror_owm = ?,last_sync_time = ?,")
		.append("modifier = ?, last_modified = ? ")
		.append("where mirror_id = ? ");
		
		Object[] params = new Object[]{
				info.getWorkgroupId(),info.getSourceId(),
				info.getState(),info.getOwm(),info.getLastSyncDate(),
				info.getModifier(),info.getModifyDate(),
				info.getInfoId().getId()
		};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		int rtv = -1;

			rtv = jtemplate.update(SQL.toString(), params);

		return rtv;
	}

	@Override
	public WorkgroupMirrorInfo query(InfoId<?> id) {
		String SQL = "select * from gp_workgroup_mirror "
				+ "where mirror_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}

		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		List<WorkgroupMirrorInfo> ainfo = jtemplate.query(SQL, params, WorkgroupMirrorMapper);

		return ainfo.size()>0 ? ainfo.get(0):null;
	}

	@Override
	public RowMapper<WorkgroupMirrorInfo> getRowMapper() {
		
		return WorkgroupMirrorMapper;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public static RowMapper<WorkgroupMirrorInfo> WorkgroupMirrorMapper = new RowMapper<WorkgroupMirrorInfo>(){

		public WorkgroupMirrorInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		
			WorkgroupMirrorInfo info = new WorkgroupMirrorInfo();
			
			InfoId<Long> id = IdKey.WORKGROUP_MIRROR.getInfoId(rs.getLong("mirror_id"));
			info.setInfoId(id);
			
			info.setSourceId(rs.getInt("source_id"));
			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setState(rs.getString("mirror_state"));
			info.setOwm(rs.getLong("mirror_owm"));
			info.setLastSyncDate(rs.getDate("last_sync_time"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			return info;
		}
		
	};
}
