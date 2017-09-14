package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKeys;
import com.gp.dao.BaseDAO;
import com.gp.info.InfoId;
import com.gp.common.IdKey;
import com.gp.dao.info.SyncMsgOutInfo;

public interface SyncMsgOutDAO extends BaseDAO<SyncMsgOutInfo>{

	public static RowMapper<SyncMsgOutInfo> MAPPER = new RowMapper<SyncMsgOutInfo>() {

		@Override
		public SyncMsgOutInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			SyncMsgOutInfo info = new SyncMsgOutInfo();
			
			InfoId<Long> id = IdKeys.getInfoId(IdKey.GP_SYNC_MSG_OUT,rs.getLong("msg_id"));
			info.setInfoId(id);
			info.setPushId(rs.getLong("push_id"));
			info.setEntityCode(rs.getString("entity_code"));
			info.setNodeCode(rs.getString("node_code"));
			info.setTraceCode(rs.getString("trace_code"));
			info.setOwm(rs.getLong("owm"));
			info.setSyncCommand(rs.getString("sync_cmd"));
			info.setMsgData(rs.getString("msg_data"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			return info;
		}};
}
