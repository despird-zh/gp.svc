package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
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
import com.gp.config.ServiceConfigurer;
import com.gp.dao.InstanceDAO;
import com.gp.info.InstanceInfo;
import com.gp.info.InfoId;

@Component("instanceDAO")
public class InstanceDAOImpl extends DAOSupport implements InstanceDAO{

	static Logger LOGGER = LoggerFactory.getLogger(InstanceDAOImpl.class);
	
	@Autowired
	public InstanceDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( InstanceInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_instances (")
			.append("instance_id,entity_code,node_code,instance_name,")
			.append("abbr,short_name,descr,email,state,")
			.append("service_url,binary_url,admin,hash_key,")
			.append("modifier, last_modified")
			.append(")values(")
			.append("?,?,?,?,")
			.append("?,?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?)");
		
		Object[] params = new Object[]{
				info.getInfoId().getId(), info.getEntityCode(),info.getNodeCode(),info.getInstanceName(),
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
		SQL.append("delete from gp_instances ")
			.append("where instance_id = ?");
		
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
	public int update(InstanceInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_instances set ")
			.append("entity_code = ?, node_code = ?, instance_name = ?,")
			.append("abbr = ?,short_name = ? ,descr = ?,email = ?,state = ?,")
			.append("service_url = ?,binary_url = ?,admin = ?,hash_key = ?,")
			.append("modifier = ?, last_modified = ? ")
			.append("where instance_id = ?");
		
		Object[] params = new Object[]{
				info.getEntityCode(),info.getNodeCode(),info.getInstanceName(),
				info.getAbbr(),info.getShortName(),info.getDescription(),info.getEmail(),info.getState(),
				info.getServiceUrl(),info.getBinaryUrl(),info.getAdmin(),info.getHashKey(),
				info.getModifier(),info.getModifyDate(),
				info.getInfoId().getId()
		};
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		int rtv = jtemplate.update(SQL.toString(),params);
		return rtv;
	}

	@Override
	public InstanceInfo query( InfoId<?> id) {
		String SQL = "select * from gp_instances "
				+ "where instance_id = ?";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		InstanceInfo ainfo = jtemplate.queryForObject(SQL, params, InstanceMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {

		this.jdbcTemplate = new JdbcTemplate(dataSource);
		
	}

	public static RowMapper<InstanceInfo> InstanceMapper = new RowMapper<InstanceInfo>(){

		@Override
		public InstanceInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			InstanceInfo info = new InstanceInfo();
			InfoId<Integer> id = IdKey.INSTANCE.getInfoId(rs.getInt("instance_id"));
			info.setInfoId(id);

			info.setEntityCode(rs.getString("entity_code"));
			info.setNodeCode(rs.getString("node_code"));
			
			info.setInstanceName(rs.getString("instance_name"));
			info.setDescription(rs.getString("descr"));
			info.setState(rs.getString("state"));
			info.setAbbr(rs.getString("abbr"));
			info.setEmail(rs.getString("email"));
			info.setShortName(rs.getString("short_name"));
			info.setBinaryUrl(rs.getString("binary_url"));
			info.setServiceUrl(rs.getString("service_url"));
			info.setAdmin(rs.getString("admin"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			return info;
		}
	};

	@Override
	public RowMapper<InstanceInfo> getRowMapper() {
		
		return InstanceMapper;
	}

	@Override
	public InstanceInfo queryByHashKey(String hashKey) {
		String SQL = "select * from gp_instances "
				+ "where hash_key = ?";
		
		Object[] params = new Object[]{				
				hashKey
			};
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		InstanceInfo ainfo = jtemplate.queryForObject(SQL, params, InstanceMapper);
		return ainfo;
	}

	@Override
	public int updateState(InfoId<Integer> instanceId, String state) {
		
		String SQL = "UPDATE gp_instances SET state = ? "
				+ "WHERE instance_id = ?";
		
		Object[] params = new Object[]{				
				state,
				instanceId.getId()
			};
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		int cnt = jtemplate.update(SQL, params);
		
		return cnt;
	}

	@Override
	public InstanceInfo queryByCodes(String entity, String node) {
		
		String SQL = "select * from gp_instances "
				+ "where entity_code = ? and node_code =?";
		
		Object[] params = new Object[]{				
				entity, node
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		}
		List<InstanceInfo> ainfo = jtemplate.query(SQL, params, InstanceMapper);
		return (ainfo != null && ainfo.size() > 0) ? ainfo.get(0) : null;
	}

}
