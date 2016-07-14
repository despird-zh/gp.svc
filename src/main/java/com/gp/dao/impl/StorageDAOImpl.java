package com.gp.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.gp.common.FlatColumns;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.StorageDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.dao.info.StorageInfo;

@Component("storageDAO")
public class StorageDAOImpl extends DAOSupport implements StorageDAO{

	public static Logger LOGGER = LoggerFactory.getLogger(StorageDAOImpl.class);
	
	@Autowired
	public StorageDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create(StorageInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_storages (")
			.append("storage_id,storage_name,capacity,used,")
			.append("setting_json,storage_type,state,")
			.append("description,modifier, last_modified")
			.append(")values(")
			.append("?,?,?,?,")
			.append("?,?,?,")
			.append("?,?,?)");
		
		InfoId<Integer> key = info.getInfoId();
		Object[] params = new Object[]{
				key.getId(),info.getStorageName(),info.getCapacity(),info.getUsed(),
				info.getSettingJson(),info.getStorageType(),info.getState(),
				info.getDescription(),info.getModifier(),info.getModifyDate()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL.toString(), Arrays.toString(params));
		}
		return jtemplate.update(SQL.toString(),params);

	}

	@Override
	public int delete(InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_storages ")
			.append("where storage_id = ?");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL.toString(), id.toString());
		}
		Object[] params = new Object[]{
			id.getId()
		};
		
		int rtv = jtemplate.update(SQL.toString(), params);
		return rtv;
	}

	@Override
	public int update(StorageInfo info, FilterMode mode, FlatColLocator ...exclcols) {
		Set<String> colset = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_storages set ");
		
		if(columnCheck(mode, colset, "storage_name")){
			SQL.append("storage_name = ?,");
			params.add(info.getStorageName());
		}
		if(columnCheck(mode, colset, "capacity")){
			SQL.append("capacity = ?,");
			params.add(info.getCapacity());
		}
		if(columnCheck(mode, colset, "used")){
			SQL.append("used = ?,");
			params.add(info.getUsed());
		}
		if(columnCheck(mode, colset, "setting_json")){
			SQL.append("setting_json = ?,");
			params.add(info.getSettingJson());
		}
		if(columnCheck(mode, colset, "storage_type")){
			SQL.append("storage_type = ?,");
			params.add(info.getStorageType());
		}
		if(columnCheck(mode, colset, "state")){
			SQL.append("state = ?,");
			params.add(info.getState());
		}
		if(columnCheck(mode, colset, "description")){
			SQL.append("description = ?,");
			params.add(info.getState());
		}
		SQL.append(" modifier = ?, last_modified = ? ")
			.append("where storage_id = ?");
		params.add(info.getModifier());
		params.add(info.getModifyDate());
		params.add(info.getInfoId().getId());
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL.toString(),params);
		}
		int rtv = jtemplate.update(SQL.toString(),params.toArray());
		return rtv;
	}

	@Override
	public StorageInfo query(InfoId<?> id) {
		String SQL = "select * from gp_storages "
				+ "where storage_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL.toString(), Arrays.toString(params));
		}
		StorageInfo ainfo = jtemplate.queryForObject(SQL, params, StorageMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
