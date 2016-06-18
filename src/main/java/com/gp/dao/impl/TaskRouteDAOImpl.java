package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.FlatColumns;
import com.gp.common.IdKey;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.TaskRouteDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.info.TaskRouteInfo;

@Component("taskRouteDAO")
public class TaskRouteDAOImpl extends DAOSupport implements TaskRouteDAO{

	static Logger LOGGER = LoggerFactory.getLogger(TaskRouteDAOImpl.class);
	
	@Autowired
	public TaskRouteDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( TaskRouteInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_task_route (")
			.append("rel_id,workgroup_id,")
			.append("task_chronical_id,task_id,task_forward_id,executor,")
			.append("owner,state,forward_time,")
			.append("modifier, last_modified")
			.append(")values(")
			.append("?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,")
			.append("?,?)");
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				key.getId(),info.getWorkgroupId(),
				info.getChronicalTaskId(),info.getTaskId(),info.getForwardTaskId(),info.getExecutor(),
				info.getOwner(),info.getState(),info.getFordwardDate(),
				info.getModifier(),info.getModifyDate()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		return jtemplate.update(SQL.toString(),params);
		
	}

	@Override
	public int delete( InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_task_route ")
			.append("where rel_id = ? ");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			id.getId()
		};
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(), params);
		return rtv;
	}

	@Override
	public int update(TaskRouteInfo info, FlatColLocator ...exclcols) {
		Set<String> cols = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_task_route set ");
		
		if(!cols.contains("workgroup_id")){
			SQL.append("workgroup_id = ?,");
			params.add(info.getWorkgroupId());
		}
		if(!cols.contains("task_chronical_id")){
			SQL.append("task_chronical_id = ?,");
			params.add(info.getChronicalTaskId());
		}
		if(!cols.contains("task_id")){
			SQL.append("task_id = ?,");
			params.add(info.getTaskId());
		}
		if(!cols.contains("task_forward_id")){
			SQL.append("task_forward_id = ?,");
			params.add(info.getForwardTaskId());
		}
		if(!cols.contains("executor")){
			SQL.append("executor = ?,");
			params.add(info.getExecutor());
		}
		if(!cols.contains("owner")){
			SQL.append("owner = ?,");
			params.add(info.getOwner());
		}
		if(!cols.contains("state")){
			SQL.append("state = ?,");
			params.add(info.getState());
		}
		if(!cols.contains("forward_time")){
			SQL.append("forward_time = ?,");
			params.add(info.getFordwardDate());
		}
		
		SQL.append("modifier = ?, last_modified = ? ")
			.append("where rel_id = ? ");
		params.add(info.getModifier());
		params.add(info.getModifyDate());
		params.add(info.getInfoId().getId());
	
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(),params.toArray());
		return rtv;
	}

	@Override
	public TaskRouteInfo query( InfoId<?> id) {
		String SQL = "select * from gp_task_route "
				+ "where rel_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		TaskRouteInfo ainfo = jtemplate.queryForObject(SQL, params, TaskRouteMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		
	}

	public static RowMapper<TaskRouteInfo> TaskRouteMapper = new RowMapper<TaskRouteInfo>(){

		@Override
		public TaskRouteInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			TaskRouteInfo info = new TaskRouteInfo();
			InfoId<Long> id = IdKey.TASK_ROUTE.getInfoId(rs.getLong("rel_id"));
			
			info.setInfoId(id);			
			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setChronicalTaskId(rs.getLong("task_chronical_id"));
			info.setTaskId(rs.getLong("task_id"));
			info.setForwardTaskId(rs.getLong("task_forward_id"));
			info.setExecutor(rs.getString("executor"));
			info.setOwner(rs.getString("owner"));
			info.setState(rs.getString("state"));
			info.setFordwardDate(rs.getTimestamp("forward_time"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			return info;
		}
	};

	@Override
	public RowMapper<TaskRouteInfo> getRowMapper() {
		
		return TaskRouteMapper;
	}
}
