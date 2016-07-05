package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.FlatColumns;
import com.gp.common.IdKey;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.WorkgroupSumDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.info.WorkgroupSumInfo;

@Component("workgroupsumDAO")
public class WorkgroupSumDAOImpl extends DAOSupport implements WorkgroupSumDAO{

	static Logger LOGGER = LoggerFactory.getLogger(WorkgroupSumDAOImpl.class);
	
	@Autowired
	public WorkgroupSumDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
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
	public int update(WorkgroupSumInfo info, FlatColLocator... excludeCols) {
		Set<String> cols = FlatColumns.toColumnSet(excludeCols);
		List<Object> params = new ArrayList<Object>();

		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_workgroup_summary set ");
		
		if(!cols.contains("workgroup_id")){
			SQL.append("workgroup_id = ?,");
			params.add(info.getWorkgroupId());
		}
		if(!cols.contains("file_sum")){
			SQL.append("file_sum = ?,");
			params.add(info.getFileSummary());
		}
		if(!cols.contains("task_sum")){
			SQL.append("task_sum = ?,");
			params.add(info.getTaskSummary());
		}
		if(!cols.contains("post_sum")){
			SQL.append("post_sum = ?,");
			params.add(info.getPostSummary());
		}
		if(!cols.contains("member_sum")){
			SQL.append("member_sum = ?,");
			params.add(info.getMemberSummary());
		}
		if(!cols.contains("publish_sum")){
			SQL.append("publish_sum = ?,");
			params.add(info.getPublishSummary());
		}
		if(!cols.contains("netdisk_sum")){
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
	public RowMapper<WorkgroupSumInfo> getRowMapper() {
		
		return WorkgroupSumMapper;
	}

	public static RowMapper<WorkgroupSumInfo> WorkgroupSumMapper = new RowMapper<WorkgroupSumInfo>(){

		@Override
		public WorkgroupSumInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			WorkgroupSumInfo info = new WorkgroupSumInfo();
			InfoId<Long> id = IdKey.WORKGROUP_SUM.getInfoId(rs.getLong("rel_id"));
			info.setInfoId(id);
			
			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setFileSummary(rs.getInt("file_sum"));
			info.setMemberSummary(rs.getInt("member_sum"));
			info.setNetdiskSummary(rs.getInt("netdisk_sum"));
			info.setPublishSummary(rs.getInt("publish_sum"));
			info.setPostSummary(rs.getInt("post_sum"));
			info.setTaskSummary(rs.getInt("task_sum"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}};
	
	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
