package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.dao.info.AttachInfo;
import com.gp.info.InfoId;

public interface AttachDAO extends BaseDAO<AttachInfo>{

	public static RowMapper<AttachInfo> AttachMapper = new RowMapper<AttachInfo>(){

		@Override
		public AttachInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			AttachInfo info = new AttachInfo();
			
			InfoId<Long> id = IdKey.ATTACHMENT.getInfoId(rs.getLong("attachment_id"));
			
			info.setInfoId(id);
			info.setResourceId(rs.getLong("resource_id"));
			info.setResourceType(rs.getString("resource_type"));
			info.setAttachName(rs.getString("attachment_name"));
			info.setSize(rs.getLong("size"));
			info.setOwner(rs.getString("owner"));
			info.setState(rs.getString("state"));
			info.setBinaryId(rs.getLong("binary_id"));
			info.setHashCode(rs.getString("hash_code"));
			info.setOwm(rs.getLong("owm"));
			info.setFormat(rs.getString("format"));
			info.setCreator(rs.getString("creator"));
			info.setCreateDate(rs.getTimestamp("create_time"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}};
}
