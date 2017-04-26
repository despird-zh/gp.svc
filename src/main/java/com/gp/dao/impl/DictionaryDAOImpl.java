package com.gp.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.gp.common.FlatColumns;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.DictionaryDAO;
import com.gp.dao.info.DictionaryInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;

@Component
public class DictionaryDAOImpl extends DAOSupport implements DictionaryDAO{

	static Logger LOGGER = LoggerFactory.getLogger(DictionaryDAOImpl.class);
	
	@Autowired
	public DictionaryDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( DictionaryInfo info) {

		Map<FlatColLocator, String> labelMap = info.getLabelMap();
		StringBuffer SQL_COL = new StringBuffer();
		StringBuffer SQL_VAL = new StringBuffer();
		SQL_COL.append("INSERT INTO gp_dictionary (")
			.append("dict_id,dict_group,")
			.append("dict_key,dict_value,");
		
		SQL_VAL.append("VALUES(")
			.append("?,?,")
			.append("?,?,?,");
		
		List<Object> plist = new ArrayList<Object>();
		plist.add(info.getInfoId().getId());
		plist.add(info.getGroup());
		plist.add(info.getKey());
		plist.add(info.getValue());

		for(Map.Entry<FlatColLocator, String> entry : labelMap.entrySet()){
			SQL_COL.append(entry.getKey().getColumn()).append(",");
			SQL_VAL.append("?,");
			plist.add(entry.getValue());
		}
		SQL_COL.append("modifier, last_modified)");
		SQL_VAL.append("?,?)");
		
		
		plist.add(info.getModifier());
		plist.add(info.getModifyDate());

		StringBuffer SQL = SQL_COL.append(SQL_VAL);
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(plist));
		}
		
		return jtemplate.update(SQL.toString(), plist.toArray());
		
	}

	@Override
	public int delete( InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("DELETE FROM gp_dictionary ")
			.append("WHERE dict_id = ?");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			id.getId()
		};
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(), params);
		return rtv;
	}

	@Override
	public int update(DictionaryInfo info, FilterMode mode,FlatColLocator ...exclcols) {
		Set<String> colset = FlatColumns.toColumnSet(exclcols);
		List<Object> plist = new ArrayList<Object>();
		Map<FlatColLocator, String> labelMap = info.getLabelMap();
		StringBuffer SQL = new StringBuffer();
		SQL.append("UPDATE gp_dictionary SET ");
		if(columnCheck(mode, colset, "dict_group")){
			SQL.append("dict_group = ?,");
			plist.add(info.getGroup());
		}
		if(columnCheck(mode, colset, "dict_key")){
			SQL.append("dict_key = ?,");
			plist.add(info.getKey());
		}
		if(columnCheck(mode, colset, "dict_value")){
			SQL.append("dict_value = ?,");
			plist.add(info.getValue());
		}
		
		for(Map.Entry<FlatColLocator, String> entry : labelMap.entrySet()){
			if(!columnCheck(mode, colset, entry.getKey().getColumn())) continue;
			
			SQL.append(entry.getKey().getColumn()).append("= ?,");
			plist.add(entry.getValue());
		}
		
		SQL.append("modifier = ?, last_modified = ? ")
			.append("where dict_id = ?");
		
		plist.add(info.getModifier());
		plist.add(info.getModifyDate());
		plist.add(info.getInfoId().getId());
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + plist.toString());
		}
	
		int rtv = jtemplate.update(SQL.toString(),plist.toArray());
		
		return rtv;
	}

	@Override
	public DictionaryInfo query( InfoId<?> id) {
		String SQL = "select * from gp_dictionary "
				+ "where dict_id = ?";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		List<DictionaryInfo> infos = jtemplate.query(SQL, params, DictionaryMapper);
		return CollectionUtils.isEmpty(infos)? null : infos.get(0);
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);		
	}


}
