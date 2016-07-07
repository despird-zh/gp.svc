package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.info.CabFolderInfo;
import com.gp.info.InfoId;

public interface CabFolderDAO extends BaseDAO<CabFolderInfo>{

	/**
	 * Query the folder information by folder name
	 * @param foldername the name of folder
	 * @param parentKey the key of parent folder. 
	 **/
	public CabFolderInfo queryByName(InfoId<Long> parentKey, String foldername);

	public List<CabFolderInfo> queryByParent(Long parentFolderId);
	

	public static RowMapper<CabFolderInfo> CabFolderMapper = new RowMapper<CabFolderInfo>(){

		@Override
		public CabFolderInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			CabFolderInfo info = new CabFolderInfo();
			InfoId<Long> id = IdKey.CAB_FOLDER.getInfoId(rs.getLong("folder_id"));
			info.setInfoId(id);

			info.setSourceId(rs.getInt("source_id"));
			info.setCabinetId(rs.getLong("cabinet_id"));
			info.setParentId(rs.getLong("folder_pid"));
			info.setEntryName(rs.getString("folder_name"));
			info.setDescription(rs.getString("descr"));
			info.setProfile(rs.getString("profile"));
			info.setProperties(rs.getString("properties"));
			info.setAclId(rs.getLong("acl_id"));
			info.setTotalSize(rs.getLong("total_size"));
			info.setOwner(rs.getString("owner"));
			info.setFolderCount(rs.getInt("folder_count"));
			info.setFileCount(rs.getInt("file_count"));
			info.setState(rs.getString("state"));
			info.setHashCode(rs.getString("hash_code"));
			info.setOwm(rs.getLong("owm"));
			info.setCreator(rs.getString("creator"));
			info.setCreateDate(rs.getTimestamp("create_time"));
			info.setClassification(rs.getString("classification"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};
}
