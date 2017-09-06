package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.dao.info.GroupMemberInfo;
import com.gp.dao.info.GroupUserInfo;
import com.gp.info.InfoId;

public interface GroupUserDAO extends BaseDAO<GroupUserInfo>{

	public int deleteByAccount(InfoId<Long> membergroupId, String account);
	
	public InfoId<Long> existByAccount(InfoId<Long> membergroupId, String account);
	
	public int deleteByGroup(InfoId<Long> membergroupId);
	
	public static RowMapper<GroupMemberInfo> GroupMemberMapper = new RowMapper<GroupMemberInfo>(){

		@Override
		public GroupMemberInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			GroupMemberInfo gminfo = new GroupMemberInfo();
			Long relid = rs.getLong("mbr_rel_id");
			InfoId<Long> rid = IdKeys.getInfoId(IdKey.GP_GROUP_USER, relid);
			gminfo.setInfoId(rid);
			gminfo.setAccount(rs.getString("account"));
			gminfo.setDescription(rs.getString("group_descr"));
			gminfo.setRole(rs.getString("role"));
			gminfo.setGroupId(rs.getLong("group_id"));
			gminfo.setGroupName(rs.getString("group_name"));
			gminfo.setGroupType(rs.getString("group_type"));
			gminfo.setManageId(rs.getLong("manage_id"));
			gminfo.setUserId(rs.getLong("user_id"));
			gminfo.setSourceId(rs.getInt("source_id"));
			gminfo.setEmail(rs.getString("email"));
			gminfo.setUserName(rs.getString("full_name"));
			gminfo.setUserType(rs.getString("user_type"));
			gminfo.setSourceName(rs.getString("source_name"));
			gminfo.setClassification(rs.getString("classification"));
			gminfo.setCreateTime(rs.getTimestamp("create_time"));
			gminfo.setPostVisible(rs.getBoolean("post_visible"));
			
			return gminfo;
		}
	};
	
	public static RowMapper<GroupUserInfo> GroupUserMapper = new RowMapper<GroupUserInfo>(){

		@Override
		public GroupUserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			GroupUserInfo info = new GroupUserInfo();
			InfoId<Long> id = IdKeys.getInfoId(IdKey.GP_GROUP_USER, rs.getLong("rel_id"));
			info.setInfoId(id);
			
			info.setAccount(rs.getString("account"));
			info.setGroupId(rs.getLong("group_id"));
			info.setRole(rs.getString("role"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};
}
