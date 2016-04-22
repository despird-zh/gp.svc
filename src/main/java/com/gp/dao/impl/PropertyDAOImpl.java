package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.dao.PropertyDAO;
import com.gp.info.InfoId;
import com.gp.info.PropertyInfo;

@Component("propertyDAO")
public class PropertyDAOImpl extends DAOSupport implements PropertyDAO{


	static Logger LOGGER = LoggerFactory.getLogger(PropertyDAOImpl.class);
	
	
	@Autowired
	public PropertyDAOImpl(DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( PropertyInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_properties (")
			.append("prop_id,prop_label,")
			.append("type,default_value,enums,format,")
			.append("modifier, last_modified")
			.append(")values(")
			.append("?,?,")
			.append("?,?,?,?,")
			.append("?,?)");
		
		Object[] params = new Object[]{
				info.getInfoId().getId(),info.getLabel(),
				info.getType(),info.getDefaultValue(),info.getEnumValues(),info.getFormat(),
				info.getModifier(),info.getModifyDate()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		return jtemplate.update(SQL.toString(),params);
	}

	@Override
	public int delete( InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_properties ")
			.append("where prop_id = ?");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			id.getId()
		};
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(), params);
		return rtv;
	}

	@Override
	public int update(PropertyInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_properties set ")
			.append("prop_label = ?,")
			.append("type = ?,default_value = ?,enums = ?,format = ?,")
			.append("modifier = ?, last_modified = ? ")
			.append("where prop_id = ?");
		
		Object[] params = new Object[]{
				info.getLabel(),
				info.getType(),info.getDefaultValue(),info.getEnumValues(),info.getFormat(),
				info.getModifier(),info.getModifyDate(),
				info.getInfoId().getId()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(),params);
		return rtv;
	}

	@Override
	public PropertyInfo query( InfoId<?> id) {
		String SQL = "select * from gp_properties "
				+ "where prop_id = ?";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		PropertyInfo ainfo = jtemplate.queryForObject(SQL, params, PropertyMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		
	}

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

	@Override
	public RowMapper<PropertyInfo> getRowMapper() {
		
		return PropertyMapper;
	}
}
