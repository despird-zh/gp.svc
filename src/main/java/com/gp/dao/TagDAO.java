package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.info.InfoId;
import com.gp.dao.info.TagInfo;

public interface TagDAO extends BaseDAO<TagInfo>{

	/**
	 * Query the tags by condition, if the category not set, it use tag type and tag name 
	 * to filter out the result.
	 * 
	 * @param tagType the tag type
	 * @param category the tag category accept null value
	 * @param tagName the tag name filter condition
	 * @return List<TagInfo> the query result  
	 **/
	public List<TagInfo> queryTags(String tagType, String category, String tagName);
	
	/**
	 * Remove the tag definition
	 * 
	 * @param tagType the type of tag POST,FILE, etc.
	 * @param category the category of tag
	 * @param tagName the name of tag.
	 **/
	public int deleteTag(String tagType, String category, String tagName);
	

	public static RowMapper<TagInfo> TagMapper = new RowMapper<TagInfo>(){

		@Override
		public TagInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			TagInfo info = new TagInfo();
			InfoId<Long> id = IdKey.TAG.getInfoId(	rs.getLong("tag_id"));
			info.setInfoId(id);

			info.setTagName(rs.getString("tag_name"));
			info.setCategory(rs.getString("category"));
			info.setTagType(rs.getString("tag_type"));
			info.setTagColor(rs.getString("tag_color"));
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};
}
