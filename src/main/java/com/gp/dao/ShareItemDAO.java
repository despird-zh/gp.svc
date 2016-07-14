package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.info.InfoId;
import com.gp.dao.info.ShareItemInfo;

public interface ShareItemDAO extends BaseDAO<ShareItemInfo>{


	public static RowMapper<ShareItemInfo> ShareItemMapper = new RowMapper<ShareItemInfo>(){

		@Override
		public ShareItemInfo mapRow(ResultSet rs, int rowNum) throws SQLException {

			ShareItemInfo info = new ShareItemInfo();
			InfoId<Long> id = IdKey.SHARE_ITEM.getInfoId(rs.getLong("share_item_id"));
			info.setInfoId(id);

			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setShareId(rs.getLong("share_id"));
			info.setCabinetId(rs.getLong("cabinet_id"));
			info.setResourceId(rs.getLong("resource_id"));
			info.setResourceType(rs.getString("resource_type"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};
}
