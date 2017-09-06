package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.dao.info.CabinetInfo;
import com.gp.info.InfoId;

public interface CabinetDAO extends BaseDAO<CabinetInfo>{
	/**
	 * Update the cabinet capacity
	 **/
	public int updateCabCapacity(InfoId<Long> cabinet, Long capacity);
	
	/**
	 * Update the cabinet storage 
	 **/
	public int changeStorage(InfoId<Long> cabinet, InfoId<Integer> storageId);
	
	/**
	 * Get cabinets by workgroupId 
	 **/
	public List<CabinetInfo> queryByWorkgroupId(InfoId<Long> wgroupkey);
	

	public static RowMapper<CabinetInfo> CabinetMapper = new RowMapper<CabinetInfo>(){

		@Override
		public CabinetInfo mapRow(ResultSet rs, int arg1) throws SQLException {

			CabinetInfo info = new CabinetInfo();
			InfoId<Long> id = IdKeys.getInfoId(IdKey.GP_CABINETS, rs.getLong("cabinet_id"));
			
			info.setInfoId(id);
			info.setSourceId(rs.getInt("source_id"));
			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setCabinetName(rs.getString("cabinet_name"));
			info.setDescription(rs.getString("descr"));
			info.setVersionable(rs.getBoolean("version_enable"));
			info.setCapacity(rs.getLong("capacity"));
			info.setStorageId(rs.getInt("storage_id"));
			info.setCreateDate(rs.getTimestamp("create_time"));
			info.setCreator(rs.getString("creator"));
			info.setCabinetType(rs.getString("cabinet_type"));
			info.setUsed(rs.getLong("used"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};
}
