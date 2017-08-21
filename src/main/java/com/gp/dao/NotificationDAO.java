package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.info.InfoId;
import com.gp.dao.info.NotificationInfo;

public interface NotificationDAO extends BaseDAO<NotificationInfo>{

	public static RowMapper<NotificationInfo> NotifcationMapper = new RowMapper<NotificationInfo>(){

		@Override
		public NotificationInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			NotificationInfo info = new NotificationInfo();
			InfoId<Long> id = IdKeys.getInfoId(IdKey.NOTIF, rs.getLong("notification_id"));
			info.setInfoId(id);
			
			info.setSourceId(rs.getInt("source_id"));
			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setQuoteExcerpt(rs.getString("quote_excerpt"));
			info.setExcerpt(rs.getString("excerpt"));
			info.setSubject(rs.getString("subject"));
			info.setSender(rs.getString("sender"));
			info.setSendTime(rs.getTimestamp("send_time"));
			info.setOperation(rs.getString("operation"));
			info.setResourceId(rs.getLong("resource_id"));
			info.setResourceType(rs.getString("resource_type"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}};
}
