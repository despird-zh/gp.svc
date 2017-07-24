package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.info.InfoId;
import com.gp.dao.info.SourceInfo;

public interface SourceDAO extends BaseDAO<SourceInfo>{

	public SourceInfo queryByHashKey(String hashKey);
	
	public int updateState(InfoId<Integer> sourceId, String state);
	
	public SourceInfo queryByCodes(String entity, String node);
	

	public static RowMapper<SourceInfo> SourceMapper = new RowMapper<SourceInfo>(){

		@Override
		public SourceInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			SourceInfo info = new SourceInfo();
			InfoId<Integer> id = IdKey.SOURCE.getInfoId(rs.getInt("source_id"));
			info.setInfoId(id);

			info.setEntityCode(rs.getString("entity_code"));
			info.setNodeCode(rs.getString("node_code"));
			info.setEntityName(rs.getString("entity_name"));
			info.setSourceName(rs.getString("source_name"));
			info.setDescription(rs.getString("descr"));
			info.setState(rs.getString("state"));
			info.setAbbr(rs.getString("abbr"));
			info.setEmail(rs.getString("email"));
			info.setShortName(rs.getString("short_name"));
			info.setBinaryUrl(rs.getString("binary_url"));
			info.setServiceUrl(rs.getString("service_url"));
			info.setAdmin(rs.getString("admin"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			return info;
		}
	};
}
