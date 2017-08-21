package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.info.InfoId;
import com.gp.dao.info.NotificationDispatchInfo;

public interface NotificationDispatchDAO extends BaseDAO<NotificationDispatchInfo>{

	public static RowMapper<NotificationDispatchInfo> NotifDispatchMapper = new RowMapper<NotificationDispatchInfo>(){

		@Override
		public NotificationDispatchInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			NotificationDispatchInfo info = new NotificationDispatchInfo();
			InfoId<Long> id = IdKeys.getInfoId(IdKey.NOTIF_DISPATCH, rs.getLong("rel_id"));
			info.setInfoId(id);
			
			info.setNotificationId(rs.getLong("notification_id"));
			info.setReceiver(rs.getString("receiver"));
			info.setTouchFlag(rs.getBoolean("touch_flag"));
			info.setTouchTime(rs.getDate("touch_time"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}};
}
