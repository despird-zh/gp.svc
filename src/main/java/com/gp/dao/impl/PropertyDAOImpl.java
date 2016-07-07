package com.gp.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.gp.common.FlatColumns;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.PropertyDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.info.PropertyInfo;

@Component("propertyDAO")
public class PropertyDAOImpl extends DAOSupport implements PropertyDAO{

	static Logger LOGGER = LoggerFactory.getLogger(PropertyDAOImpl.class);
	
	@Autowired
	public PropertyDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
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
	public int update(PropertyInfo info, FlatColLocator ...exclcols) {
		Set<String> cols = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_properties set ");
		if(!cols.contains("")){
			SQL.append("prop_label = ?,");
			params.add(info.getLabel());
		}
		if(!cols.contains("type")){
			SQL.append("type = ?,");
			params.add(info.getType());
		}
		if(!cols.contains("default_value")){
			SQL.append("default_value = ?,");
			params.add(info.getDefaultValue());
		}
		if(!cols.contains("enums")){
			SQL.append("enums = ?,");
			params.add(info.getEnumValues());
		}
		if(!cols.contains("format")){
			SQL.append("format = ?,");
			params.add(info.getFormat());
		}
	
		SQL.append("modifier = ?, last_modified = ? ")
			.append("where prop_id = ?");
		params.add(info.getModifier());
		params.add(info.getModifyDate());
		params.add(info.getInfoId().getId());
	
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(),params.toArray());
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


}
