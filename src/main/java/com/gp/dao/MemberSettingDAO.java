package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.info.InfoId;
import com.gp.dao.info.MemberSettingInfo;

public interface MemberSettingDAO extends BaseDAO<MemberSettingInfo>{

	public MemberSettingInfo queryByMember(InfoId<Long> manageId, String account);
	
	static RowMapper<MemberSettingInfo> MemberSettingMapper = new RowMapper<MemberSettingInfo>(){

		@Override
		public MemberSettingInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			MemberSettingInfo info = new MemberSettingInfo();
			InfoId<Long> id = IdKeys.getInfoId(IdKey.GP_MBR_SETTING, rs.getLong("rel_id"));
			info.setInfoId(id);
			
			info.setAccount(rs.getString("account"));
			info.setGroupType(rs.getString("group_type"));
			info.setManageId(rs.getLong("manage_id"));
			info.setPostVisible(rs.getBoolean("post_visible"));
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			return info;
		}
		
	};
}
