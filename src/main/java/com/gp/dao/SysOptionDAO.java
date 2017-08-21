package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.info.InfoId;
import com.gp.dao.info.SysOptionInfo;

public interface SysOptionDAO extends BaseDAO<SysOptionInfo>{

	public List<SysOptionInfo> queryAll() ;
	
	public List<SysOptionInfo> queryByGroup(String groupKey) ;
	
	public SysOptionInfo queryByKey(String optKey) ;
	
	public int updateByKey(String optionKey, String optionValue);
	

	public static RowMapper<SysOptionInfo> SysOptionMapper = new RowMapper<SysOptionInfo>(){

		@Override
		public SysOptionInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			SysOptionInfo info = new SysOptionInfo();
			InfoId<Integer> id = IdKeys.getInfoId(IdKey.SYS_OPTION, rs.getInt("sys_opt_id"));
			info.setInfoId(id);

			info.setOptionGroup(rs.getString("opt_group"));
			info.setOptionKey(rs.getString("opt_key"));
			info.setOptionValue(rs.getString("opt_value"));
			info.setDescription(rs.getString("descr"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}};
}
