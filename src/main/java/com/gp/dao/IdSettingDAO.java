package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.info.IdSettingInfo;
import com.gp.info.Identifier;

public interface IdSettingDAO{
	
	IdSettingInfo queryByIdKey( Identifier idKey);
	
	int updateByIdKey(String modifier, Identifier idKey, Long nextValue);
	

	public static RowMapper<IdSettingInfo> IdSettringMapper = new RowMapper<IdSettingInfo>(){

		@Override
		public IdSettingInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			IdSettingInfo idsetting = new IdSettingInfo();
			idsetting.setIdKey(rs.getString("id_key"));
			idsetting.setIdName(rs.getString("id_name"));
			idsetting.setCurrValue(rs.getLong("curr_val"));
			idsetting.setStepIncrement(rs.getInt("step_inc"));
			idsetting.setLength(rs.getInt("length"));
			idsetting.setPrefix(rs.getString("prefix"));
			idsetting.setPadChar(rs.getString("pad_char"));
			idsetting.setModifier(rs.getString("modifier"));
			idsetting.setModifyDate(rs.getTimestamp("last_modified"));
			
			return idsetting;
		}};
}
