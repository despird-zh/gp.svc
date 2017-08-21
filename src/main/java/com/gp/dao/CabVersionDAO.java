package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.dao.info.CabVersionInfo;
import com.gp.info.InfoId;

public interface CabVersionDAO extends BaseDAO<CabVersionInfo>{

	public List<CabVersionInfo> queryByFileId(Long fileid);
	

	public static RowMapper<CabVersionInfo> CabVersionMapper = new RowMapper<CabVersionInfo>(){

		@Override
		public CabVersionInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			CabVersionInfo info = new CabVersionInfo();
			InfoId<Long> id = IdKeys.getInfoId(IdKey.CAB_VERSION, rs.getLong("version_id"));
			info.setInfoId(id);
			info.setSourceId(rs.getInt("source_id"));
			info.setCabinetId(rs.getLong("cabinet_id"));
			info.setParentId(rs.getLong("folder_id"));
			info.setFileId(rs.getLong("file_id"));
			info.setFileName(rs.getString("file_name"));
			info.setDescription(rs.getString("descr"));
			info.setProfile(rs.getString("profile"));
			info.setProperties(rs.getString("properties"));
			info.setVersionLabel(rs.getString("version_label"));
			info.setSize(rs.getLong("size"));
			info.setOwner(rs.getString("owner"));
			info.setCommentOn(rs.getBoolean("comment_on"));
			info.setVersion(rs.getString("version"));
			info.setState(rs.getString("state"));
			info.setBinaryId(rs.getLong("binary_id"));
			info.setFormat(rs.getString("format"));
			info.setOwm(rs.getLong("owm"));
			info.setCreator(rs.getString("creator"));
			info.setCreateDate(rs.getTimestamp("create_time"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};
}
