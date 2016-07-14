package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.info.InfoId;
import com.gp.dao.info.ShareInfo;

public interface ShareDAO extends BaseDAO<ShareInfo>{

	public static RowMapper<ShareInfo> ShareMapper = new RowMapper<ShareInfo>(){

		@Override
		public ShareInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			ShareInfo info = new ShareInfo();
			
			InfoId<Long> id = IdKey.SHARE.getInfoId(rs.getLong("share_id"));
			info.setInfoId(id);
			
			info.setSourceId(rs.getInt("source_id"));
			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setSharer(rs.getString("sharer"));
			info.setTarget(rs.getString("target"));
			info.setOwm(rs.getLong("owm"));
			info.setShareKey(rs.getString("share_key"));
			info.setShareDate(rs.getTimestamp("share_time"));
			info.setExpireDate(rs.getTimestamp("expire_time"));
			info.setAccessLimit(rs.getInt("access_limit"));
			info.setAccessTimes(rs.getInt("access_times"));
			info.setShareName(rs.getString("share_name"));
			info.setDescription(rs.getString("descr"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};
}
