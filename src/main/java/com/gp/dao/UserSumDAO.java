package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.info.InfoId;
import com.gp.info.UserSumInfo;

public interface UserSumDAO extends BaseDAO<UserSumInfo>{

	UserSumInfo queryByAccount(String account);
	

	public static RowMapper<UserSumInfo> UserSumMapper = new RowMapper<UserSumInfo>(){

		@Override
		public UserSumInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserSumInfo info = new UserSumInfo();
			InfoId<Long> id = IdKey.USER_SUM.getInfoId(rs.getLong("rel_id"));
			info.setInfoId(id);
			
			info.setAccount(rs.getString("account"));
			info.setFileSummary(rs.getInt("file_sum"));
			info.setPostSummary(rs.getInt("post_sum"));
			info.setShareSummary(rs.getInt("share_sum"));
			info.setTaskSummary(rs.getInt("task_sum"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			return info;
		}};
		
}
