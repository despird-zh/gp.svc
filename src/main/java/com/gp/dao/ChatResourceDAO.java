package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.info.ChatResourceInfo;
import com.gp.info.InfoId;

public interface ChatResourceDAO extends BaseDAO<ChatResourceInfo>{

	public static RowMapper<ChatResourceInfo> ChatResourceMapper = new RowMapper<ChatResourceInfo>(){

		@Override
		public ChatResourceInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			ChatResourceInfo info = new ChatResourceInfo();
			InfoId<Long> id = IdKey.CHAT_RESC.getInfoId(rs.getLong("rel_id"));
			info.setInfoId(id);
			
			info.setChatId(rs.getLong("chat_id"));
			info.setResourceId(rs.getLong("resource_id"));
			info.setResourceType(rs.getString("resource_type"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}};
}
