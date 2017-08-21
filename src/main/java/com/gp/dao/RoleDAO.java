package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.info.InfoId;
import com.gp.dao.info.RoleInfo;

public interface RoleDAO extends BaseDAO<RoleInfo>{
	
	List<RoleInfo> queryAll();
	

	public static RowMapper<RoleInfo> ROLE_Mapper = new RowMapper<RoleInfo>(){

		@Override
		public RoleInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			RoleInfo info = new RoleInfo();
			InfoId<Integer> id = IdKeys.getInfoId(IdKey.ROLE,rs.getInt("tag_id"));
			info.setInfoId(id);
			info.setRoleAbbr(rs.getString("role_abbr"));
			info.setRoleName(rs.getString("role_name"));
			info.setDescription(rs.getString("descr"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};
}
