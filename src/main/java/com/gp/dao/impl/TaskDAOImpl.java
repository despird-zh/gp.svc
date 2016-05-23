package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

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
import com.gp.dao.TaskDAO;
import com.gp.info.InfoId;
import com.gp.info.TaskInfo;

@Component("taskDAO")
public class TaskDAOImpl extends DAOSupport implements TaskDAO{

	static Logger LOGGER = LoggerFactory.getLogger(TaskDAOImpl.class);
	
	@Autowired
	public TaskDAOImpl(@Qualifier(ServiceConfigurator.DATA_SRC)DataSource dataSource) {
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
	public int update(TaskInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_tasks set ")
			.append("workgroup_id = ?,hash_code = ?,source_id = ? ,")
			.append("task_chronical_id = ?,task_name = ?,descr = ?,weight = ?,")
			.append("state = ?,due_time = ?,exec_opinion = ?,exec_time = ?,")
			.append("complete_time = ?,assignee_json = ?,owner = ?,executor = ?,")
			.append("modifier = ?, last_modified = ? ")
			.append("where task_id = ? ");
		
		Object[] params = new Object[]{
				info.getWorkgroupId(),info.getHashCode(),info.getSourceId(),
				info.getTaskChronicalId(),info.getTaskName(),info.getDescription(),info.getWeight(),
				info.getState(),info.getDueDate(),info.getOpinion(),info.getExecuteDate(),
				info.getCompleteDate(),info.getAsignee(),info.getOwner(),info.getExecutor(),
				info.getModifier(),info.getModifyDate(),
				info.getInfoId().getId()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(),params);
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

	public static RowMapper<TaskInfo> TaskMapper = new RowMapper<TaskInfo>(){

		@Override
		public TaskInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			TaskInfo info  =  new TaskInfo();
			InfoId<Long> id = IdKey.TASK.getInfoId(rs.getLong("task_id"));
			info.setInfoId(id);
			info.setSourceId(rs.getInt("source_id"));
			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setTaskChronicalId(rs.getLong("task_chronical_id"));
			info.setTaskName(rs.getString("task_name"));
			info.setDescription(rs.getString("descr"));
			info.setWeight(rs.getDouble("weight"));
			info.setState(rs.getString("state"));
			info.setDueDate(rs.getTimestamp("due_time"));
			info.setOpinion(rs.getString("exec_opinion"));
			info.setExecuteDate(rs.getTimestamp("exec_time"));
			info.setCompleteDate(rs.getTimestamp("complete_time"));
			info.setAsignee(rs.getString("assignee_json"));
			info.setOwner(rs.getString("owner"));
			info.setExecutor(rs.getString("executor"));
			info.setHashCode(rs.getString("hash_code"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};

	@Override
	public RowMapper<TaskInfo> getRowMapper() {
		
		return TaskMapper;
	}
}
