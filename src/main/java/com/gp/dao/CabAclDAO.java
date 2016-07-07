package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.info.CabAclInfo;
import com.gp.info.InfoId;

public interface CabAclDAO extends BaseDAO<CabAclInfo>{

	public static RowMapper<CabAclInfo> CabAclMapper = new RowMapper<CabAclInfo>(){

		@Override
		public CabAclInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			CabAclInfo info = new CabAclInfo();
			InfoId<Long> id = IdKey.CAB_ACL.getInfoId(rs.getLong("acl_id"));
			
			info.setInfoId(id);
			info.setAclHash(rs.getString("acl_hash"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}};
}
