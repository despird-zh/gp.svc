package com.gp.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.gp.common.FlatColumns.FilterMode;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.SourceDAO;
import com.gp.dao.info.SourceInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;

@Component("sourceDAO")
public class SourceDAOImpl extends DAOSupport implements SourceDAO{

	static Logger LOGGER = LoggerFactory.getLogger(SourceDAOImpl.class);
	
	@Autowired
	public SourceDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( SourceInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_sources (")
			.append("source_id,entity_code,node_code,source_name,")
			.append("abbr,short_name,descr,email,state,")
			.append("service_url,binary_url,admin,hash_key,")
			.append("modifier, last_modified")
			.append(")values(")
			.append("?,?,?,?,")
			.append("?,?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?)");
		
		Object[] params = new Object[]{
				info.getInfoId().getId(), info.getEntityCode(),info.getNodeCode(),info.getSourceName(),
				info.getAbbr(),info.getShortName(),info.getDescription(),info.getEmail(),info.getState(),
				info.getServiceUrl(),info.getBinaryUrl(),info.getAdmin(),info.getHashKey(),
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
		SQL.append("delete from gp_sources ")
			.append("where source_id = ?");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			id.getId()
		};
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(), params);
		return rtv;
	}

	@Override
	public int update(SourceInfo info, FilterMode mode, FlatColLocator ...exclcols) {
		Set<String> colset = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_sources set ");
		
		if(columnCheck(mode, colset, "entity_code")){
			SQL.append("entity_code = ?,");
			params.add(info.getEntityCode());
		}
		if(columnCheck(mode, colset, "node_code")){
			SQL.append("node_code = ?,");
			params.add(info.getNodeCode());
		}
		if(columnCheck(mode, colset, "source_name")){
			SQL.append("source_name = ?,");
			params.add(info.getSourceName());
		}
		if(columnCheck(mode, colset, "abbr")){
			SQL.append("abbr = ?,");
			params.add(info.getAbbr());
		}
		if(columnCheck(mode, colset, "short_name")){
			SQL.append("short_name = ? ,");
			params.add(info.getShortName());
		}
		if(columnCheck(mode, colset, "descr")){
			SQL.append("descr = ?,");
			params.add(info.getDescription());
		}
		if(columnCheck(mode, colset, "email")){
			SQL.append("email = ?,");
			params.add(info.getEmail());
		}
		if(columnCheck(mode, colset, "state")){
			SQL.append("state = ?,");
			params.add(info.getState());
		}
		if(columnCheck(mode, colset, "service_url")){
		SQL.append("service_url = ?,");
		params.add(info.getServiceUrl());
		}
		if(columnCheck(mode, colset, "binary_url")){
		SQL.append("binary_url = ?,");
		params.add(info.getBinaryUrl());
		}
		if(columnCheck(mode, colset, "admin")){
		SQL.append("admin = ?,");
		params.add(info.getAdmin());
		}
		if(columnCheck(mode, colset, "hash_key")){
		SQL.append("hash_key = ?,");
		params.add(info.getHashKey());
		}
	
		SQL.append("modifier = ?, last_modified = ? ")
			.append("where source_id = ?");
		params.add(info.getModifier());
		params.add(info.getModifyDate());
		params.add(info.getInfoId().getId());
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, params);
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		int rtv = jtemplate.update(SQL.toString(),params.toArray());
		return rtv;
	}

	@Override
	public SourceInfo query( InfoId<?> id) {
		String SQL = "select * from gp_sources "
				+ "where source_id = ?";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		SourceInfo ainfo = jtemplate.queryForObject(SQL, params, SourceMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {

		this.jdbcTemplate = new JdbcTemplate(dataSource);
		
	}



	@Override
	public SourceInfo queryByHashKey(String hashKey) {
		String SQL = "select * from gp_sources "
				+ "where hash_key = ?";
		
		Object[] params = new Object[]{				
				hashKey
			};
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		SourceInfo ainfo = jtemplate.queryForObject(SQL, params, SourceMapper);
		return ainfo;
	}

	@Override
	public int updateState(InfoId<Integer> sourceId, String state) {
		
		String SQL = "UPDATE gp_sources SET state = ? "
				+ "WHERE source_id = ?";
		
		Object[] params = new Object[]{				
				state,
				sourceId.getId()
			};
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		int cnt = jtemplate.update(SQL, params);
		
		return cnt;
	}

	@Override
	public SourceInfo queryByCodes(String entity, String node) {
		
		String SQL = "select * from gp_sources "
				+ "where entity_code = ? and node_code =?";
		
		Object[] params = new Object[]{				
				entity, node
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		}
		List<SourceInfo> ainfo = jtemplate.query(SQL, params, SourceMapper);
		return (ainfo != null && ainfo.size() > 0) ? ainfo.get(0) : null;
	}

}
