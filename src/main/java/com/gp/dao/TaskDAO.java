package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.info.InfoId;
import com.gp.info.TaskInfo;

public interface TaskDAO extends BaseDAO<TaskInfo>{


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
}
