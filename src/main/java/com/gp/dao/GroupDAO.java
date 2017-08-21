package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.dao.info.GroupInfo;
import com.gp.info.InfoId;

public interface GroupDAO extends BaseDAO<GroupInfo>{

	public int deleteByName(InfoId<Long> workgroupId, String type, String group);
	
	public GroupInfo queryByName(InfoId<Long> workgroupId, String type, String group);


	public static RowMapper<GroupInfo> GroupMapper = new RowMapper<GroupInfo>(){

		@Override
		public GroupInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			GroupInfo info = new GroupInfo();
			InfoId<Long> id = IdKeys.getInfoId(IdKey.GROUP, rs.getLong("group_id"));
			
			info.setInfoId(id);

			info.setManageId(rs.getLong("manage_id"));
			info.setGroupName(rs.getString("group_name"));
			info.setGroupType(rs.getString("group_type"));
			info.setDescription(rs.getString("descr"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			return info;
		}
	};
}
