package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.info.InfoId;
import com.gp.dao.info.PropertyInfo;

public interface PropertyDAO extends BaseDAO<PropertyInfo>{


	public static RowMapper<PropertyInfo> PropertyMapper = new RowMapper<PropertyInfo>(){

		@Override
		public PropertyInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			PropertyInfo info = new PropertyInfo();
			InfoId<Long> id = IdKey.PROPERTY.getInfoId(rs.getLong("prop_id"));
			info.setInfoId(id);

			info.setLabel(rs.getString("prop_label"));
			info.setType(rs.getString("type"));
			info.setDefaultValue(rs.getString("default_value"));
			info.setEnumValues(rs.getString("enums"));
			info.setFormat(rs.getString("format"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};
}
