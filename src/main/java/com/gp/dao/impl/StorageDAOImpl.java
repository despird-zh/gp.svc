package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.config.ServiceConfigurator;
import com.gp.dao.StorageDAO;
import com.gp.info.InfoId;
import com.gp.info.StorageInfo;

@Component("storageDAO")
public class StorageDAOImpl extends DAOSupport implements StorageDAO{

	public static Logger LOGGER = LoggerFactory.getLogger(StorageDAOImpl.class);
	
	@Autowired
	public StorageDAOImpl(@Qualifier(ServiceConfigurator.DATA_SRC)DataSource dataSource) {
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
	public int update(StorageInfo info) {
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_storages set ")
			.append("storage_name = ?,capacity = ?,used = ?,")
			.append("setting_json = ?,storage_type = ?,state = ?,")
			.append("description = ?, modifier = ?, last_modified = ? ")
			.append("where storage_id = ?");
		
		Object[] params = new Object[]{
				info.getStorageName(),info.getCapacity(),info.getUsed(),
				info.getSettingJson(),info.getStorageType(),info.getState(),
				info.getDescription(),info.getModifier(),info.getModifyDate(),
				info.getInfoId().getId()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL.toString(), Arrays.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(),params);
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
	public RowMapper<StorageInfo> getRowMapper() {
		
		return StorageMapper;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public static RowMapper<StorageInfo> StorageMapper = new RowMapper<StorageInfo>(){

		@Override
		public StorageInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			StorageInfo info = new StorageInfo();
			
			InfoId<Integer> id = IdKey.STORAGE.getInfoId(rs.getInt("storage_id"));
			info.setInfoId(id);

			info.setStorageName(rs.getString("storage_name"));
			info.setCapacity(rs.getLong("capacity"));
			info.setUsed(rs.getLong("used"));
			info.setSettingJson(rs.getString("setting_json"));
			info.setStorageType(rs.getString("storage_type"));
			info.setState(rs.getString("state"));
			info.setDescription(rs.getString("description"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};
}
