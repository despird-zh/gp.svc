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
import com.gp.config.ServiceConfigurer;
import com.gp.dao.WorkgroupUserDAO;
import com.gp.info.InfoId;
import com.gp.info.WorkgroupUserInfo;

@Component("workgroupUserDAO")
public class WorkgroupUserDAOImpl extends DAOSupport implements WorkgroupUserDAO{

	static Logger LOGGER = LoggerFactory.getLogger(WorkgroupUserDAOImpl.class);
	
	@Autowired
	public WorkgroupUserDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( WorkgroupUserInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_workgroup_user (")
			.append("rel_id,workgroup_id,global_account,owm,")
			.append("account,role,modifier,last_modified")
			.append(")values(")
			.append("?,?,?,?,")
			.append("?,?,?,?)");
		
		InfoId<Long> key = info.getInfoId();
		
		Object[] params = new Object[]{
				key.getId(),info.getWorkgroupId(),info.getGlobalAccount(),info.getOwm(),
				info.getAccount(),info.getRole(),info.getModifier(),info.getModifyDate()
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
		SQL.append("delete from gp_workgroup_user ")
			.append("where rel_id = ? ");
		
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
	public int update(WorkgroupUserInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_workgroup_user set ")
			.append("workgroup_id = ?,global_account = ?,owm = ?,")
			.append("account = ?, role = ?, modifier = ?, last_modified = ? ")
			.append("where rel_id = ? ");
		
		Object[] params = new Object[]{
				info.getWorkgroupId(),info.getGlobalAccount(),info.getOwm(),
				info.getAccount(),info.getRole(),info.getModifier(),info.getModifyDate(),
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
	public WorkgroupUserInfo query( InfoId<?> id) {
		String SQL = "select * from gp_workgroup_user "
				+ "where rel_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		WorkgroupUserInfo ainfo = jtemplate.queryForObject(SQL, params, WorkgroupUserMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	RowMapper<WorkgroupUserInfo> WorkgroupUserMapper = new RowMapper<WorkgroupUserInfo>(){

		@Override
		public WorkgroupUserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			WorkgroupUserInfo info = new WorkgroupUserInfo();
			InfoId<Long> id = IdKey.WORKGROUP_USER.getInfoId(rs.getLong("rel_id"));
			info.setInfoId(id);

			info.setAccount(rs.getString("account"));
			info.setRole(rs.getString("role"));
			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setGlobalAccount(rs.getString("global_account"));
			info.setOwm(rs.getLong("owm"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			return info;
		}
	};

	@Override
	public RowMapper<WorkgroupUserInfo> getRowMapper() {
		
		return WorkgroupUserMapper;
	}

	@Override
	public WorkgroupUserInfo queryByAccount(Long workgroupId, String account) {
		String SQL = "select * from gp_workgroup_user "
				+ "where workgroup_id = ? and account = ?";
		
		Object[] params = new Object[]{				
				workgroupId,
				account
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		List<WorkgroupUserInfo> infos = jtemplate.query(SQL, params, WorkgroupUserMapper);
		if(null == infos || infos.size() ==0){
			
			return null;
		}
		return infos.get(0);
	}
}
