package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.info.InfoId;
import com.gp.info.MessageDispatchInfo;

public interface MessageDispatchDAO extends BaseDAO<MessageDispatchInfo>{

	public static RowMapper<MessageDispatchInfo> MessageDispatchMapper = new RowMapper<MessageDispatchInfo>(){

		@Override
		public MessageDispatchInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			MessageDispatchInfo info = new MessageDispatchInfo();
			InfoId<Long> id = IdKey.MESSAGE.getInfoId(rs.getLong("rel_id"));
			info.setInfoId(id);

			info.setMessageId(rs.getLong("message_id"));
			info.setMessageContent(rs.getString("msg_content"));
			info.setAccount(rs.getString("account"));
			info.setGlobalAccount(rs.getString("global_account"));
			info.setTouchFlag(rs.getBoolean("touch_flag"));
			info.setTouchTime(rs.getDate("touch_time"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};
}
