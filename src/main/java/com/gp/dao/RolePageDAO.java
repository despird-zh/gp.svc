package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.info.FlatColLocator;
import com.gp.info.FlatColumn;
import com.gp.info.InfoId;
import com.gp.info.RolePageInfo;

public interface RolePageDAO extends BaseDAO<RolePageInfo>{

	public int update(InfoId<Integer> roleId, InfoId<Integer> pageId, Map<FlatColLocator, Boolean> perms);
	

	public static RowMapper<RolePageInfo> RolePageMapper = new RowMapper<RolePageInfo>(){

		@Override
		public RolePageInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			RolePageInfo rpage = new RolePageInfo();
			
			InfoId<Integer> mid = IdKey.PAGE.getInfoId(rs.getInt("rel_id"));
			rpage.setInfoId(mid);
			
			rpage.setPageId(rs.getInt("page_id"));
			rpage.setRoleId(rs.getInt("role_id"));
			
			for(int i = 1; i <= PageDAO.COLUMN_COUNT ; i++){
				Boolean val = rs.getBoolean("act_perm_"+i);
				
				FlatColumn col = new FlatColumn("act_perm_", i);
				rpage.putColValue(col, val);			
				
			}
			
			rpage.setModifier(rs.getString("modifier"));
			rpage.setModifyDate(rs.getTimestamp("last_modified"));
			return rpage;
		}
		
	};
}
