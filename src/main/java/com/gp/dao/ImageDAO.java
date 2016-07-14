package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.dao.info.ImageInfo;
import com.gp.info.InfoId;

public interface ImageDAO extends BaseDAO<ImageInfo>{

	public ImageInfo query(InfoId<Long> infoid, String parentPath);
	

	public static RowMapper<ImageInfo> ImageMapper = new RowMapper<ImageInfo>(){
		
		@Override
		public ImageInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			ImageInfo info = new ImageInfo();
			InfoId<Long> id = IdKey.IMAGE.getInfoId(rs.getLong("image_id"));
			info.setInfoId(id);
			
			info.setImageName(rs.getString("image_name"));
			info.setFormat(rs.getString("image_format"));
			info.setExtension(rs.getString("image_ext"));
			info.setTouchTime(rs.getTimestamp("touch_time"));
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			return info;
		}
	};
}
