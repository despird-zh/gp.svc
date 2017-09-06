package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.info.InfoId;
import com.gp.dao.info.StorageInfo;

public interface StorageDAO extends BaseDAO<StorageInfo>{


	public static RowMapper<StorageInfo> StorageMapper = new RowMapper<StorageInfo>(){

		@Override
		public StorageInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			StorageInfo info = new StorageInfo();
			
			InfoId<Integer> id = IdKeys.getInfoId(IdKey.GP_STORAGES, rs.getInt("storage_id"));
			info.setInfoId(id);

			info.setStorageName(rs.getString("storage_name"));
			info.setCapacity(rs.getLong("capacity"));
			info.setUsed(rs.getLong("used"));
			info.setSettingJson(rs.getString("setting_json"));
			info.setStorageType(rs.getString("storage_type"));
			info.setState(rs.getString("state"));
			info.setDescription(rs.getString("description"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};
}
