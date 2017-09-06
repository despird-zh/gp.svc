package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.info.FlatColumn;
import com.gp.info.InfoId;
import com.gp.dao.info.UserRoleInfo;

public interface UserRoleDAO extends BaseDAO<UserRoleInfo>{

	public static final int COLUMN_COUNT = 6;
	
	
	public static RowMapper<UserRoleInfo> UserRoleMapper = new RowMapper<UserRoleInfo>(){

		@Override
		public UserRoleInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserRoleInfo ur = new UserRoleInfo();
			
			InfoId<Long> mid = IdKeys.getInfoId(IdKey.GP_USER_ROLE,rs.getLong("rel_id"));
			ur.setInfoId(mid);
			
			ur.setUserId(rs.getLong("user_id"));
		
			for(int i = 1; i <= COLUMN_COUNT ; i++){
				Integer val = rs.getInt("role_"+i);
				if(val > 0){
					FlatColumn col = new FlatColumn("role_", i);
					ur.putColValue(col, val);
				}				
			}
			
			ur.setModifier(rs.getString("modifier"));
			ur.setModifyDate(rs.getTimestamp("last_modified"));
			return ur;
		}
		
	};
}
