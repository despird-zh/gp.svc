package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.info.ChatInfo;
import com.gp.info.InfoId;

public interface ChatDAO extends BaseDAO<ChatInfo>{

	public static RowMapper<ChatInfo> ChatMapper = new RowMapper<ChatInfo>(){

		@Override
		public ChatInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			ChatInfo info = new ChatInfo();
			InfoId<Long> id = IdKey.CHAT.getInfoId(rs.getLong("chat_id"));
			info.setInfoId(id);
			
			info.setChatType(rs.getString("chat_type"));
			info.setCreateTime(rs.getTimestamp("create_time"));
			info.setMemberGroupId(rs.getLong("mbr_group_id"));
			info.setSponsor(rs.getString("sponsor"));
			info.setEphemeral(rs.getBoolean("is_ephemeral"));
			info.setTopic(rs.getString("topic"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}};
}
