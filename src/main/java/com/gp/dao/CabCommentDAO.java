package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.dao.info.CabCommentInfo;
import com.gp.info.InfoId;

public interface CabCommentDAO extends BaseDAO<CabCommentInfo>{

	public static RowMapper<CabCommentInfo> CabCommentMapper = new RowMapper<CabCommentInfo>(){

		@Override
		public CabCommentInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			CabCommentInfo info = new CabCommentInfo();
			
			InfoId<Long> id = IdKey.CAB_COMMENT.getInfoId(	rs.getLong("comment_id"));
			info.setInfoId(id);
			
			info.setSourceId(rs.getInt("source_id"));
			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setParentId(rs.getLong("comment_pid"));
			info.setDocId(rs.getLong("file_id"));
			info.setAuthor(rs.getString("author"));
			info.setOwner(rs.getString("owner"));
			info.setContent(rs.getString("content"));
			info.setState(rs.getString("state"));
			info.setCommentDate(rs.getTimestamp("comment_time"));
			info.setOwm(rs.getLong("owm"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};
}
