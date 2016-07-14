package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.dao.info.CabFileInfo;
import com.gp.info.InfoId;

public interface CabFileDAO extends BaseDAO<CabFileInfo>{
	
	public List<CabFileInfo> queryByParent(Long parentFolderId);
	

	public static RowMapper<CabFileInfo> CabFileMapper = new RowMapper<CabFileInfo>(){

		@Override
		public CabFileInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			CabFileInfo info = new CabFileInfo();
			InfoId<Long> id = IdKey.CAB_FILE.getInfoId(rs.getLong("file_id"));
			info.setInfoId(id);
	
			info.setSourceId(rs.getInt("source_id"));
			info.setCabinetId(rs.getLong("cabinet_id"));
			info.setParentId(rs.getLong("folder_id"));
			info.setEntryName(rs.getString("file_name"));
			info.setDescription(rs.getString("descr"));
			info.setProfile(rs.getString("profile"));
			info.setProperties(rs.getString("properties"));
			info.setAclId(rs.getLong("acl_id"));
			info.setSize(rs.getLong("size"));
			info.setOwner(rs.getString("owner"));
			info.setCommentOn(rs.getBoolean("comment_on"));
			info.setVersion(rs.getString("version"));
			info.setState(rs.getString("state"));
			info.setBinaryId(rs.getLong("binary_id"));
			info.setFormat(rs.getString("format"));
			info.setCreator(rs.getString("creator"));
			info.setCreateDate(rs.getTimestamp("create_time"));
			info.setHashCode(rs.getString("hash_code"));
			info.setClassification(rs.getString("classification"));
			info.setOwm(rs.getLong("owm"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};
}
