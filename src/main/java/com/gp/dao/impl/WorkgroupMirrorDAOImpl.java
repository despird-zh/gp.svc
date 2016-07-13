package com.gp.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.gp.common.FlatColumns;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.WorkgroupMirrorDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.info.WorkgroupMirrorInfo;

@Component("workgroupMirrorDAO")
public class WorkgroupMirrorDAOImpl extends DAOSupport implements WorkgroupMirrorDAO{

	static Logger LOGGER = LoggerFactory.getLogger(WorkgroupMirrorDAOImpl.class);

	@Autowired
	public WorkgroupMirrorDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
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
	public int update(WorkgroupMirrorInfo info,FilterMode mode, FlatColLocator ...exclcols) {
		Set<String> colset = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_workgroup_mirror set ");
		
		if(columnCheck(mode, colset, "workgroup_id")){
			SQL.append("workgroup_id = ?,");
			params.add(info.getWorkgroupId());
		}
		if(columnCheck(mode, colset, "source_id")){
			SQL.append("source_id = ? ,");
			params.add(info.getSourceId());
		}
		if(columnCheck(mode, colset, "mirror_state")){
			SQL.append("mirror_state = ?,");
			params.add(info.getState());
		}
		if(columnCheck(mode, colset, "mirror_owm")){
			SQL.append("mirror_owm = ?,");
			params.add(info.getOwm());
		}
		if(columnCheck(mode, colset, "last_sync_time")){
			SQL.append("last_sync_time = ?,");
			params.add(info.getLastSyncDate());
		}

		SQL.append("modifier = ?, last_modified = ? ")
			.append("where mirror_id = ? ");
		params.add(info.getModifier());
		params.add(info.getModifyDate());
		params.add(info.getInfoId().getId());

		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		int rtv = -1;

			rtv = jtemplate.update(SQL.toString(), params.toArray());

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
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
