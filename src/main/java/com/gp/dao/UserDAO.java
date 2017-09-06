package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.info.InfoId;
import com.gp.dao.info.UserInfo;

public interface UserDAO extends BaseDAO<UserInfo>{

	/**
	 * check the account exist or not 
	 **/
	public boolean existAccount( String account);

	/**
	 * Query user information by account 
	 **/
	public UserInfo queryByAccount( String account);

	/**
	 * change user password 
	 **/
	public int changePassword( String account, String password);

	/**
	 * query account information. 
	 **/
	public List<UserInfo> queryAccounts( String accountname, String entity, String[] type);

	public static RowMapper<UserInfo> UserMapper = new RowMapper<UserInfo>(){

		@Override
		public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserInfo info = new UserInfo();
			InfoId<Long> id = IdKeys.getInfoId(IdKey.GP_USERS, rs.getLong("user_id"));
			info.setInfoId(id);

			info.setSourceId(rs.getInt("source_id"));
			info.setAccount(rs.getString("account"));
			info.setType(rs.getString("type"));
			info.setMobile(rs.getString("mobile"));
			info.setPhone(rs.getString("phone"));
			info.setFullName(rs.getString("full_name"));
			info.setEmail(rs.getString("email"));
			info.setPassword(rs.getString("password"));
			info.setState(rs.getString("state"));
			info.setCreateDate(rs.getTimestamp("create_time"));
			info.setExtraInfo(rs.getString("extra_info"));
			info.setRetryTimes(rs.getInt("retry_times"));
			info.setLastLogonDate(rs.getDate("last_logon"));
			info.setLanguage(rs.getString("language"));
			info.setTimezone(rs.getString("timezone"));
			info.setPublishCabinet(rs.getLong("publish_cabinet_id"));
			info.setNetdiskCabinet(rs.getLong("netdisk_cabinet_id"));
			info.setGlobalAccount(rs.getString("global_account"));
			info.setStorageId(rs.getInt("storage_id"));
			info.setAvatarId(rs.getLong("avatar_id"));
			info.setClassification(rs.getString("classification"));
			info.setSignature(rs.getString("signature"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			return info;
		}};
}
