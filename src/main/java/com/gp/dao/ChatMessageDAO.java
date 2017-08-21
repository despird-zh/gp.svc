package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.info.InfoId;
import com.gp.dao.info.ChatMessageInfo;

public interface ChatMessageDAO extends BaseDAO<ChatMessageInfo>{

	public static RowMapper<ChatMessageInfo> MessageMapper = new RowMapper<ChatMessageInfo>(){

		@Override
		public ChatMessageInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			ChatMessageInfo info = new ChatMessageInfo();
			InfoId<Long> id = IdKeys.getInfoId(IdKey.MESSAGE, rs.getLong("message_id"));
			info.setInfoId(id);
			
			info.setChatId(rs.getLong("chat_id"));
			info.setMessageType(rs.getString("msg_type"));
			info.setMessageContent(rs.getString("msg_content"));
			info.setResourceId(rs.getLong("resource_id"));
			info.setResourceType( rs.getString("resource_type"));
			info.setSender(rs.getString("sender"));
			info.setSendTime(rs.getTimestamp("send_time"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};
}
