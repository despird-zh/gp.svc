package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

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
import com.gp.dao.OrgUserDAO;
import com.gp.info.InfoId;
import com.gp.info.OrgUserInfo;

@Component("orgUserDAO")
public class OrgUserDAOImpl extends DAOSupport implements OrgUserDAO{

	Logger LOGGER = LoggerFactory.getLogger(OrgUserDAOImpl.class);
	
	@Autowired
	public OrgUserDAOImpl(@Qualifier(ServiceConfigurator.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( OrgUserInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_org_user (")
			.append("rel_id,")
			.append("org_id,account,")
			.append("modifier, last_modified")
			.append(")values(")
			.append("?,")
			.append("?,?,")
			.append("?,?)");
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{				
				key.getId(),
				info.getOrgId(),info.getAccount(),
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
		SQL.append("delete from gp_org_user ")
			.append("where rel_id = ?");
		
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
	public int update(OrgUserInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_org_user set ")
			.append("org_id = ?,")
			.append("account = ? ,")
			.append("modifier = ?, last_modified = ? ")
			.append("where rel_id = ?");
		
		Object[] params = new Object[]{
				info.getOrgId(),
				info.getAccount(),
				info.getModifier(),info.getModifyDate(),
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
	public OrgUserInfo query( InfoId<?> id) {

		String SQL = "select * from gp_org_user "
				+ "where rel_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		OrgUserInfo ainfo = jtemplate.queryForObject(SQL, params, OrgUserMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		
	}

	public static RowMapper<OrgUserInfo> OrgUserMapper = new RowMapper<OrgUserInfo>(){

		@Override
		public OrgUserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			OrgUserInfo info = new OrgUserInfo();
			InfoId<Long> id = IdKey.ORG_USER.getInfoId(rs.getLong("rel_id"));
			
			info.setInfoId(id);

			info.setOrgId(rs.getLong("org_id"));
			info.setAccount(rs.getString("account"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			return info;
		}
	};

	@Override
	public RowMapper<OrgUserInfo> getRowMapper() {
		
		return OrgUserMapper;
	}

	@Override
	public int deleteByAccount(InfoId<Long> orgId, String account) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_org_user ")
			.append("where org_id = ? and account = ?");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			orgId.getId(),
			account
		};
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(), params);
		return rtv;
	}

	@Override
	public int deleteByOrgHier(InfoId<Long> orgId) {
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_org_user ")
			.append("where org_id = ?");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			orgId.getId()
		};
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(), params);
		return rtv;
	}
}
