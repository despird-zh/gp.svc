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
import com.gp.dao.TaskRouteDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.dao.info.TaskRouteInfo;

@Component
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
	public int update(TaskRouteInfo info, FilterMode mode, FlatColLocator ...exclcols) {
		Set<String> colset = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_task_route set ");
		
		if(columnCheck(mode, colset, "workgroup_id")){
			SQL.append("workgroup_id = ?,");
			params.add(info.getWorkgroupId());
		}
		if(columnCheck(mode, colset, "task_chronical_id")){
			SQL.append("task_chronical_id = ?,");
			params.add(info.getChronicalTaskId());
		}
		if(columnCheck(mode, colset, "task_id")){
			SQL.append("task_id = ?,");
			params.add(info.getTaskId());
		}
		if(columnCheck(mode, colset, "task_forward_id")){
			SQL.append("task_forward_id = ?,");
			params.add(info.getForwardTaskId());
		}
		if(columnCheck(mode, colset, "executor")){
			SQL.append("executor = ?,");
			params.add(info.getExecutor());
		}
		if(columnCheck(mode, colset, "owner")){
			SQL.append("owner = ?,");
			params.add(info.getOwner());
		}
		if(columnCheck(mode, colset, "state")){
			SQL.append("state = ?,");
			params.add(info.getState());
		}
		if(columnCheck(mode, colset, "forward_time")){
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



}
