package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.info.InfoId;
import com.gp.info.MessageInfo;

public interface MessageDAO extends BaseDAO<MessageInfo>{

	public static RowMapper<MessageInfo> MessageMapper = new RowMapper<MessageInfo>(){

		@Override
		public MessageInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			MessageInfo info = new MessageInfo();
			InfoId<Long> id = IdKey.MESSAGE.getInfoId(rs.getLong("message_id"));
			info.setInfoId(id);
			
			info.setSourceId(rs.getInt("source_id"));
			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setCabinetId(rs.getLong("cabinet_id"));
			info.setResourceId(rs.getLong("resource_id"));
			info.setResourceType(rs.getString("resource_type"));
			info.setOperation(rs.getString("operation"));
			info.setSendAccount(rs.getString("send_account"));
			info.setReplyEnable(rs.getBoolean("reply_enable"));
			info.setCategory(rs.getString("category"));
			info.setMsgDictKey(rs.getString("msg_dict_key"));
			info.setMsgParams(rs.getString("msg_params_json"));
			info.setSendGlobalAccount(rs.getString("send_global_account"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};
}
