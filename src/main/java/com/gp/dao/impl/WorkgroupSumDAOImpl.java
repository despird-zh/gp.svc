package com.gp.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.gp.common.FlatColumns;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.common.DataSourceHolder;
import com.gp.dao.WorkgroupSumDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.dao.info.WorkgroupSumInfo;

@Component
public class WorkgroupSumDAOImpl extends DAOSupport implements WorkgroupSumDAO{

	static Logger LOGGER = LoggerFactory.getLogger(WorkgroupSumDAOImpl.class);
	
	@Autowired
	public WorkgroupSumDAOImpl(@Qualifier(DataSourceHolder.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create(WorkgroupSumInfo info) {
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("INSERT INTO gp_workgroup_summary(")
			.append("rel_id, workgroup_id, file_sum, task_sum,")
			.append("member_sum, publish_sum, post_sum, netdisk_sum,")
			.append("modifier, last_modified")
			.append(")VALUES(")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?")
			.append(")");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				key.getId(),info.getWorkgroupId(),info.getFileSummary(),info.getTaskSummary(),
				info.getMemberSummary(),info.getPublishSummary(), info.getPostSummary(), info.getNetdiskSummary(),
				info.getModifier(),info.getModifyDate()
		};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);

		return jtemplate.update(SQL.toString(),params);
	}

	@Override
	public int delete(InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_workgroup_summary ")
			.append("where rel_id = ? ");
		
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
	public int update(WorkgroupSumInfo info, FilterMode mode, FlatColLocator... excludeCols) {
		Set<String> colset = FlatColumns.toColumnSet(excludeCols);
		List<Object> params = new ArrayList<Object>();

		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_workgroup_summary set ");
		
		if(columnCheck(mode, colset, "workgroup_id")){
			SQL.append("workgroup_id = ?,");
			params.add(info.getWorkgroupId());
		}
		if(columnCheck(mode, colset, "file_sum")){
			SQL.append("file_sum = ?,");
			params.add(info.getFileSummary());
		}
		if(columnCheck(mode, colset, "task_sum")){
			SQL.append("task_sum = ?,");
			params.add(info.getTaskSummary());
		}
		if(columnCheck(mode, colset, "post_sum")){
			SQL.append("post_sum = ?,");
			params.add(info.getPostSummary());
		}
		if(columnCheck(mode, colset, "member_sum")){
			SQL.append("member_sum = ?,");
			params.add(info.getMemberSummary());
		}
		if(columnCheck(mode, colset, "publish_sum")){
			SQL.append("publish_sum = ?,");
			params.add(info.getPublishSummary());
		}
		if(columnCheck(mode, colset, "netdisk_sum")){
			SQL.append("netdisk_sum = ?,");
			params.add(info.getNetdiskSummary());
		}
		
		SQL.append("modifier = ?,last_modified = ? ")
		.append("where rel_id = ? ");
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
	public WorkgroupSumInfo query(InfoId<?> id) {
		String SQL = "select * from gp_user_summary "
				+ "where rel_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);

		List<WorkgroupSumInfo>	ainfo = jtemplate.query(SQL, params, WorkgroupSumMapper);

		return CollectionUtils.isEmpty(ainfo) ? null : ainfo.get(0);
	}
	
	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public WorkgroupSumInfo queryByWId(InfoId<Long> wgroupId) {
		
		String SQL = "select * from gp_workgroup_summary "
				+ "where workgroup_id = ? ";
		
		Object[] params = new Object[]{				
				wgroupId.getId()
			};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);

		List<WorkgroupSumInfo>	ainfo = jtemplate.query(SQL, params, WorkgroupSumMapper);

		return CollectionUtils.isEmpty(ainfo) ? null : ainfo.get(0);
	}

}
