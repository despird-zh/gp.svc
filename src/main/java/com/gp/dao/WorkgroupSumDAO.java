package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.info.InfoId;
import com.gp.dao.info.WorkgroupSumInfo;

public interface WorkgroupSumDAO extends BaseDAO<WorkgroupSumInfo>{

	public WorkgroupSumInfo queryByWId(InfoId<Long> wgroupId);
	
	public static RowMapper<WorkgroupSumInfo> WorkgroupSumMapper = new RowMapper<WorkgroupSumInfo>(){

		@Override
		public WorkgroupSumInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			WorkgroupSumInfo info = new WorkgroupSumInfo();
			InfoId<Long> id = IdKeys.getInfoId(IdKey.WORKGROUP_SUM, rs.getLong("rel_id"));
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
}
