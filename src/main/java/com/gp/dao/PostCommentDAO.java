package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.info.InfoId;
import com.gp.info.PostCommentInfo;

public interface PostCommentDAO extends BaseDAO<PostCommentInfo>{


	public static RowMapper<PostCommentInfo> PostCommentMapper = new RowMapper<PostCommentInfo>(){

		@Override
		public PostCommentInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			PostCommentInfo info = new PostCommentInfo();
			InfoId<Long> id = IdKey.POST_COMMENT.getInfoId(rs.getLong("comment_id"));
			info.setInfoId(id);
			info.setSourceId(rs.getInt("source_id"));
			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setParentId(rs.getLong("comment_pid"));
			info.setPostId(rs.getLong("post_id"));
			info.setHashCode(rs.getString("hash_code"));
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
