package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.info.InfoId;
import com.gp.dao.info.TaskRouteInfo;

public interface TaskRouteDAO extends BaseDAO<TaskRouteInfo>{

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
}
