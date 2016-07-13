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
import com.gp.dao.TaskDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.info.TaskInfo;

@Component("taskDAO")
public class TaskDAOImpl extends DAOSupport implements TaskDAO{

	static Logger LOGGER = LoggerFactory.getLogger(TaskDAOImpl.class);
	
	@Autowired
	public TaskDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( TaskInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_tasks (")
			.append("source_id,workgroup_id,task_id,hash_code,")
			.append("task_chronical_id,task_name,descr,weight,")
			.append("state,due_time,exec_opinion,exec_time,")
			.append("complete_time,assignee_json,owner,executor,")
			.append("modifier, last_modified")
			.append(")values(")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?)");
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				info.getSourceId(),info.getWorkgroupId(),key.getId(),info.getHashCode(),
				info.getTaskChronicalId(),info.getTaskName(),info.getDescription(),info.getWeight(),
				info.getState(),info.getDueDate(),info.getOpinion(),info.getExecuteDate(),
				info.getCompleteDate(),info.getAsignee(),info.getOwner(),info.getExecutor(),
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
		SQL.append("delete from gp_tasks ")
			.append("where task_id = ? ");
		
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
	public int update(TaskInfo info, FilterMode mode, FlatColLocator ...exclcols) {
		Set<String> colset = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_tasks set ");
		if(columnCheck(mode, colset, "workgroup_id")){
			SQL.append("workgroup_id = ?,");
			params.add(info.getWorkgroupId());
		}
		if(columnCheck(mode, colset, "hash_code")){
			SQL.append("hash_code = ?,");
			params.add(info.getHashCode());
		}
		if(columnCheck(mode, colset, "source_id")){
			SQL.append("source_id = ? ,");
			params.add(info.getSourceId());
		}
		if(columnCheck(mode, colset, "task_chronical_id")){
			SQL.append("task_chronical_id = ?,");
			params.add(info.getTaskChronicalId());
		}
		if(columnCheck(mode, colset, "task_name")){
			SQL.append("task_name = ?,");
			params.add(info.getTaskName());
		}
		if(columnCheck(mode, colset, "descr")){
			SQL.append("descr = ?,");
			params.add(info.getDescription());
		}
		if(columnCheck(mode, colset, "weight")){
			SQL.append("weight = ?,");
			params.add(info.getWeight());
		}
		if(columnCheck(mode, colset, "state")){
			SQL.append("state = ?,");
			params.add(info.getState());
		}
		if(columnCheck(mode, colset, "due_time")){
			SQL.append("due_time = ?,");
			params.add(info.getDueDate());
		}
		if(columnCheck(mode, colset, "exec_opinion")){
			SQL.append("exec_opinion = ?,");
			params.add(info.getOpinion());
		}
		if(columnCheck(mode, colset, "exec_time")){
			SQL.append("exec_time = ?,");
			params.add(info.getExecuteDate());
		}
		if(columnCheck(mode, colset, "complete_time")){
			SQL.append("complete_time = ?,");
			params.add(info.getCompleteDate());
		}
		if(columnCheck(mode, colset, "assignee_json")){
			SQL.append("assignee_json = ?,");
			params.add(info.getAsignee());
		}
		if(columnCheck(mode, colset, "owner")){
			SQL.append("owner = ?,");
			params.add(info.getOwner());
		}
		if(columnCheck(mode, colset, "executor")){
			SQL.append("executor = ?,");
			params.add(info.getExecutor());
		}
		
		SQL.append("modifier = ?, last_modified = ? ")
			.append("where task_id = ? ");
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
	public TaskInfo query( InfoId<?> id) {
		String SQL = "select * from gp_tasks "
				+ "where task_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		TaskInfo ainfo = jtemplate.queryForObject(SQL, params, TaskMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}


}
