package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.info.InfoId;
import com.gp.dao.info.WorkgroupMirrorInfo;

public interface WorkgroupMirrorDAO extends BaseDAO<WorkgroupMirrorInfo>{

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
