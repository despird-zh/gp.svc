package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.dao.info.AttachRelInfo;
import com.gp.info.InfoId;

public interface AttachRelDAO extends BaseDAO<AttachRelInfo>{

	public static RowMapper<AttachRelInfo> AttachRelMapper = new RowMapper<AttachRelInfo>(){

		@Override
		public AttachRelInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			
			AttachRelInfo info = new AttachRelInfo();
			InfoId<Long> id = IdKeys.getInfoId(IdKey.ATTACH_REL, rs.getLong("rel_id"));
			
			info.setInfoId(id);

			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setAttachId(rs.getLong("atta_id"));
			info.setAttachName(rs.getString("atta_name"));
			info.setAttachType(rs.getString("atta_type"));
			info.setResourceId(rs.getLong("resource_id"));
			info.setResourceType(rs.getString("resource_type"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
		
	};
}
