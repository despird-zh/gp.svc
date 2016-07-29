package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.info.InfoId;
import com.gp.dao.info.PostInfo;

public interface PostDAO extends BaseDAO<PostInfo>{


	public static RowMapper<PostInfo> PostMapper = new RowMapper<PostInfo>(){

		@Override
		public PostInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			PostInfo info = new PostInfo();
			InfoId<Long> id = IdKey.POST.getInfoId(rs.getLong("post_id"));
			
			info.setInfoId(id);
			info.setSourceId(rs.getInt("source_id"));
			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setOwner(rs.getString("owner"));
			info.setContent(rs.getString("content"));
			info.setExcerpt(rs.getString("excerpt"));
			info.setSubject(rs.getString("subject"));
			info.setState(rs.getString("state"));
			info.setScope(rs.getString("scope"));
			info.setCommentOn(rs.getBoolean("comment_on"));
			info.setPostType(rs.getString("post_type"));
			info.setCommentCount(rs.getInt("comment_count"));
			info.setUpvoteCount(rs.getInt("upvote_count"));
			info.setDownvoteCount(rs.getInt("downvote_count"));
			info.setPostDate(rs.getDate("post_time"));
			info.setHashCode(rs.getString("hash_code"));
			info.setOwm(rs.getLong("owm"));
			info.setPriority(rs.getInt("priority"));
			info.setClassification(rs.getString("classification"));

			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};
}
