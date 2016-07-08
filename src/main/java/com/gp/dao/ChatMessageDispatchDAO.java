package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.info.InfoId;
import com.gp.info.ChatMessageDispatchInfo;

public interface ChatMessageDispatchDAO extends BaseDAO<ChatMessageDispatchInfo>{

	public static RowMapper<ChatMessageDispatchInfo> MessageDispatchMapper = new RowMapper<ChatMessageDispatchInfo>(){

		@Override
		public ChatMessageDispatchInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			ChatMessageDispatchInfo info = new ChatMessageDispatchInfo();
			InfoId<Long> id = IdKey.MESSAGE.getInfoId(rs.getLong("rel_id"));
			info.setInfoId(id);

			info.setMessageId(rs.getLong("message_id"));
			info.setReceiver(rs.getString("receiver"));
			info.setTouchFlag(rs.getBoolean("touch_flag"));
			info.setTouchTime(rs.getDate("touch_time"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};
}
