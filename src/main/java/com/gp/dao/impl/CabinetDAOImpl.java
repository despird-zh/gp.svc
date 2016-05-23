package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.config.ServiceConfigurator;
import com.gp.dao.CabinetDAO;
import com.gp.info.CabinetInfo;
import com.gp.info.InfoId;

@Component("cabinetDAO")
public class CabinetDAOImpl extends DAOSupport implements CabinetDAO{

	Logger LOGGER = LoggerFactory.getLogger(CabinetDAOImpl.class);
	
	@Autowired
	public CabinetDAOImpl(@Qualifier(ServiceConfigurator.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( CabinetInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_cabinets (")
			.append("source_id,workgroup_id, cabinet_id,used,")
			.append("cabinet_name,descr,version_enable,capacity,cabinet_type,")
			.append("storage_id,creator,create_time,modifier, last_modified")
			.append(")values(")
			.append("?,?,?,?,")
			.append("?,?,?,?,?,")
			.append("?,?,?,?,?)");
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				info.getSourceId(),info.getWorkgroupId(),key.getId(),info.getUsed(),
				info.getCabinetName(),info.getDescription(),info.isVersionable(),info.getCapacity(),info.getCabinetType(),
				info.getStorageId(),info.getCreator(),info.getCreateDate(),
				info.getModifier(),info.getModifyDate()
		};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		return jtemplate.update(SQL.toString(),params);

	}

	@Override
	public int delete( InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_cabinets ")
			.append("where cabinet_id = ? ");
		
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
	public int update(CabinetInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_cabinets set ")
			.append("workgroup_id = ?,cabinet_name =?,cabinet_type = ?,used = ?,source_id = ?,")
			.append("descr = ?,version_enable = ?,capacity = ?,storage_id = ?,")
			.append("creator = ?,create_time = ?,modifier = ?, last_modified = ? ")
			.append("where cabinet_id = ? ");
		
		Object[] params = new Object[]{
				info.getWorkgroupId(),info.getCabinetName(),info.getCabinetType(),info.getUsed(),info.getSourceId(),
				info.getDescription(),info.isVersionable(),info.getCapacity(),info.getStorageId(),
				info.getCreator(),info.getCreateDate(),info.getModifier(),info.getModifyDate(),
				info.getInfoId().getId()
		};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		int rtv = jtemplate.update(SQL.toString(),params);
		return rtv;
	}

	@Override
	public CabinetInfo query( InfoId<?> id) {
		String SQL = "select * from gp_cabinets "
				+ "where cabinet_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		List<CabinetInfo> ainfo = jtemplate.query(SQL, params, CabinetMapper);
		return ainfo.size()>0 ? ainfo.get(0):null;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public static RowMapper<CabinetInfo> CabinetMapper = new RowMapper<CabinetInfo>(){

		@Override
		public CabinetInfo mapRow(ResultSet rs, int arg1) throws SQLException {

			CabinetInfo info = new CabinetInfo();
			InfoId<Long> id = IdKey.CABINET.getInfoId(	rs.getLong("cabinet_id"));
			
			info.setInfoId(id);
			info.setSourceId(rs.getInt("source_id"));
			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setCabinetName(rs.getString("cabinet_name"));
			info.setDescription(rs.getString("descr"));
			info.setVersionable(rs.getBoolean("version_enable"));
			info.setCapacity(rs.getLong("capacity"));
			info.setStorageId(rs.getInt("storage_id"));
			info.setCreateDate(rs.getTimestamp("create_time"));
			info.setCreator(rs.getString("creator"));
			info.setCabinetType(rs.getString("cabinet_type"));
			info.setUsed(rs.getLong("used"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};


	@Override
	public RowMapper<CabinetInfo> getRowMapper() {
		
		return CabinetMapper;
	}

	@Override
	public int updateCabCapacity(InfoId<Long> cabinet, Long capacity) {
		String SQL = "UPDATE gp_cabinets SET capacity = ? WHERE cabinet_id = ?";
		
		Object[] params = new Object[]{
				capacity,cabinet.getId()
		};
		
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		int rtv = jtemplate.update(SQL.toString(),params);
		return rtv;
	}

	@Override
	public int changeStorage(InfoId<Long> cabinet, InfoId<Integer> storageId) {
		String SQL = "UPDATE gp_cabinets SET storage_id = ? WHERE cabinet_id = ?";
		
		Object[] params = new Object[]{
				storageId.getId(),cabinet.getId()
		};
		
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		int rtv = jtemplate.update(SQL.toString(),params);
		return rtv;
	}

	@Override
	public List<CabinetInfo> queryByWorkgroupId(InfoId<Long> wgroupid) {
		String SQL = "SELECT * FROM gp_cabinets WHERE workgroup_id = ?";
		Object[] params = new Object[]{
				wgroupid.getId()
		};
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		List<CabinetInfo> cinfos = jtemplate.query(SQL, params, CabinetMapper);
		return cinfos;
	}
}
