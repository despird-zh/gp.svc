package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.info.CabSumInfo;
import com.gp.info.InfoId;

public interface CabSumDAO extends BaseDAO<CabSumInfo>{


	public static RowMapper<CabSumInfo> CabSumMapper = new RowMapper<CabSumInfo>(){

		@Override
		public CabSumInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			CabSumInfo info = new CabSumInfo();
			InfoId<Long> id = IdKey.USER_SUM.getInfoId(rs.getLong("rel_id"));
			info.setInfoId(id);
			
			info.setResourceId(rs.getLong("resource_id"));
			info.setResourceType(rs.getString("resource_type"));
			info.setFileSummary(rs.getInt("file_sum"));
			info.setFolderSummary(rs.getInt("folder_sum"));
			info.setTotalSize(rs.getLong("total_size"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			return info;
		}};
}
