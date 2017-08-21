package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.dao.info.BinaryInfo;
import com.gp.info.InfoId;

public interface BinaryDAO extends BaseDAO<BinaryInfo>{


	public static RowMapper<BinaryInfo> BinaryMapper = new RowMapper<BinaryInfo>(){

		@Override
		public BinaryInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			BinaryInfo info = new BinaryInfo();
			InfoId<Long> id = IdKeys.getInfoId(IdKey.BINARY, rs.getLong("binary_id"));
			
			info.setInfoId(id);
			
			info.setSourceId(rs.getInt("source_id"));
			info.setSize(rs.getLong("size"));
			info.setStorageId(rs.getInt("storage_id"));
			info.setHashCode(rs.getString("hash_code"));
			info.setStoreLocation(rs.getString("store_location"));
			info.setState(rs.getString("state"));
			info.setCreator(rs.getString("creator"));
			info.setCreateDate(rs.getTimestamp("create_time"));
			info.setFormat(rs.getString("format"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			return info;
		}
	};
}
