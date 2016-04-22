package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.dao.OrgHierDAO;
import com.gp.info.InfoId;
import com.gp.info.OrgHierInfo;


@Component("orgHierDAO")
public class OrgHierDAOImpl extends DAOSupport implements OrgHierDAO{

	Logger LOGGER = LoggerFactory.getLogger(OrgHierDAOImpl.class);
		
	@Autowired
	public OrgHierDAOImpl(DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( OrgHierInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_org_hier (")
			.append("org_pid,org_id,group_id,admin,")
			.append("org_level,org_name,descr,manager,")
			.append("email, modifier, last_modified")
			.append(")values(")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?)");
		
		InfoId<Long> key = info.getInfoId();
		
		Object[] params = new Object[]{
				info.getParentOrg(),key.getId(),info.getGroupId(),info.getAdmin(),
				info.getLevel(),info.getOrgName(),info.getDescription(),info.getManager(),
				info.getEmail(),info.getModifier(),info.getModifyDate()
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
		SQL.append("delete from gp_org_hier ")
			.append("where org_id = ?");
		
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
	public int update(OrgHierInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_org_hier set ")
			.append("org_pid = ?,group_id = ?,admin = ?,")
			.append("org_level = ? ,org_name = ?,manager = ?, email = ?,")
			.append("descr = ?,modifier = ?, last_modified = ? ")
			.append("where org_id = ? ");
		
		Object[] params = new Object[]{
				info.getParentOrg(),info.getGroupId(),info.getAdmin(),
				info.getLevel(),info.getOrgName(),info.getManager(),info.getEmail(),
				info.getDescription(),info.getModifier(),info.getModifyDate(),
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
	public OrgHierInfo query( InfoId<?> id) {
		String SQL = "select * from gp_org_hier "
				+ "where org_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		OrgHierInfo ainfo = jtemplate.queryForObject(SQL, params, OrgHierMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public static RowMapper<OrgHierInfo> OrgHierMapper = new RowMapper<OrgHierInfo>(){

		@Override
		public OrgHierInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			OrgHierInfo info = new OrgHierInfo();
			InfoId<Long> id = IdKey.ORG_HIER.getInfoId(rs.getLong("org_id"));
			info.setInfoId(id);
			
			info.setGroupId(rs.getLong("group_id"));
			info.setLevel(rs.getString("org_level"));
			info.setParentOrg(rs.getLong("org_pid"));
			info.setOrgName(rs.getString("org_name"));
			info.setAdmin(rs.getString("admin"));
			info.setEmail(rs.getString("email"));
			info.setManager(rs.getString("manager"));
			info.setDescription(rs.getString("descr"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			return info;
		}
	};

	@Override
	public RowMapper<OrgHierInfo> getRowMapper() {
		// TODO Auto-generated method stub
		return OrgHierMapper;
	}
}
