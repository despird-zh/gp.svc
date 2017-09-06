package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.info.InfoId;
import com.gp.dao.info.TagRelInfo;

public interface TagRelDAO extends BaseDAO<TagRelInfo>{

	/**
	 * Delete the tag attached on resource
	 * @param resId the id of resource
	 * @param tagName the tag to be detached 
	 **/
	public int delete(InfoId<?> resId, String tagName);

	public TagRelInfo query(InfoId<?> resId, String tagName);
	

	public static RowMapper<TagRelInfo> TagRelMapper = new RowMapper<TagRelInfo>(){

		@Override
		public TagRelInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			TagRelInfo info = new TagRelInfo();
			InfoId<Long> id = IdKeys.getInfoId(IdKey.GP_TAG_REL, rs.getLong("rel_id"));
			info.setInfoId(id);
			
			info.setResourceId(rs.getLong("resource_id"));
			info.setTagName(rs.getString("tag_name"));
			info.setResourceType(rs.getString("resource_type"));
			info.setCategory(rs.getString("category"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};
}
