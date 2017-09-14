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

import com.gp.common.DataSourceHolder;
import com.gp.common.FlatColumns;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.dao.BinaryDAO;
import com.gp.dao.info.BinaryInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;

@Component
public class BinaryDAOImpl extends DAOSupport implements BinaryDAO{

	Logger LOGGER = LoggerFactory.getLogger(BinaryDAOImpl.class);
	
	@Autowired
	public BinaryDAOImpl(@Qualifier(DataSourceHolder.DATA_SRC)DataSource dataSource) {
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
	public int update(BinaryInfo info,FilterMode mode, FlatColLocator ...exclcols) {
		Set<String> colset = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_binaries set ");
		
		if(columnCheck(mode, colset, "size")){
			SQL.append("size = ?,");
			params.add(info.getSize());
		}
		if(columnCheck(mode, colset, "source_id")){
			SQL.append("source_id = ? ,");
			params.add(info.getSourceId());
		}
		if(columnCheck(mode, colset, "storage_id")){
			SQL.append("storage_id = ?,");
			params.add(info.getStorageId());
		}
		if(columnCheck(mode, colset, "hash_code")){
			SQL.append("hash_code = ?,");
			params.add(info.getHashCode());
		}
		if(columnCheck(mode, colset, "store_location")){
			SQL.append("store_location = ?,");
			params.add(info.getStoreLocation());
		}
		if(columnCheck(mode, colset, "state")){
			SQL.append("state = ?,");
			params.add(info.getState());
		}
		if(columnCheck(mode, colset, "format")){
			SQL.append("format = ?, ");
			params.add(info.getFormat());
		}
		if(columnCheck(mode, colset, "creator")){
			SQL.append("creator = ?,");
			params.add(info.getCreator());
		}
		if(columnCheck(mode, colset, "create_time")){
			SQL.append("create_time = ?,");
			params.add(info.getCreateDate());
		}
		
		SQL.append("modifier = ?,last_modified = ? ");
		SQL.append("where binary_id = ?  ");
		params.add(info.getModifier());
		params.add(info.getModifyDate());
		params.add(info.getInfoId().getId());
	
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(),params.toArray());
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


}
