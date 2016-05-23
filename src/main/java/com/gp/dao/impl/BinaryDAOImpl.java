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
import com.gp.dao.BinaryDAO;
import com.gp.info.BinaryInfo;
import com.gp.info.InfoId;

@Component("binaryDAO")
public class BinaryDAOImpl extends DAOSupport implements BinaryDAO{

	Logger LOGGER = LoggerFactory.getLogger(BinaryDAOImpl.class);
	
	@Autowired
	public BinaryDAOImpl(@Qualifier(ServiceConfigurator.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( BinaryInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_binaries (")
			.append("source_id,binary_id,size,storage_id,")
			.append("hash_code,store_location,state,")
			.append("format,creator,create_time,")
			.append("modifier,last_modified")
			.append(")values(")
			.append("?,?,?,?,")
			.append("?,?,?,")
			.append("?,?,?,")
			.append("?,?)");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				info.getSourceId(),key.getId(),info.getSize(),info.getStorageId(),
				info.getHashCode(),info.getStoreLocation(),info.getState(),
				info.getFormat(),info.getCreator(),info.getCreateDate(),
				info.getModifier(),info.getModifyDate()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int cnt = jtemplate.update(SQL.toString(),params);
		
		return cnt;
	}

	@Override
	public int delete( InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_binaries ")
			.append("where binary_id = ? ");
		
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
	public int update(BinaryInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_binaries set ")
		.append("size = ?,source_id = ? ,storage_id = ?,")
		.append("hash_code = ?,store_location = ?,state = ?,")
		.append("format = ?, creator = ?,create_time = ?,")
		.append("modifier = ?,last_modified = ? ")
		.append("where binary_id = ?  ");
		
		Object[] params = new Object[]{
				info.getSize(),info.getSourceId(),info.getStorageId(),
				info.getHashCode(),info.getStoreLocation(),info.getState(),
				info.getFormat(), info.getCreator(),info.getCreateDate(),
				info.getModifier(),info.getModifyDate(),
				info.getInfoId().getId()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(),params);
		return rtv;
	}

	@Override
	public BinaryInfo query( InfoId<?> id) {
		String SQL = "select * from gp_binaries "
				+ "where binary_id = ?";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		List<BinaryInfo> ainfo = jtemplate.query(SQL, params, BinaryMapper);
		return ainfo.size()>0 ? ainfo.get(0):null;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public static RowMapper<BinaryInfo> BinaryMapper = new RowMapper<BinaryInfo>(){

		@Override
		public BinaryInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			BinaryInfo info = new BinaryInfo();
			InfoId<Long> id = IdKey.BINARY.getInfoId(rs.getLong("binary_id"));
			
			info.setInfoId(id);
			
			info.setSourceId(rs.getInt("source_id"));
			info.setSize(rs.getLong("size"));
			info.setStorageId(rs.getInt("storage_id"));
			info.setHashCode(rs.getString("hash_code"));
			info.setStoreLocation(rs.getString("store_location"));
			info.setState(rs.getString("state"));
			info.setCreator(rs.getString("creator"));
			info.setCreateDate(rs.getTimestamp("create_time"));
			info.setFormat(rs.getString("format"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			return info;
		}
	};
	
	@Override
	public RowMapper<BinaryInfo> getRowMapper() {
		
		return BinaryMapper;
	}
}
