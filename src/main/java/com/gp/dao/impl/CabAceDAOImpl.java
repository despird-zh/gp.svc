package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
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
import com.gp.dao.CabAceDAO;
import com.gp.info.CabAceInfo;
import com.gp.info.InfoId;

@Component("cabAceDAO")
public class CabAceDAOImpl extends DAOSupport implements CabAceDAO{

	static Logger LOGGER = LoggerFactory.getLogger(CabAceDAOImpl.class);
	
	@Autowired
	public CabAceDAOImpl(@Qualifier(ServiceConfigurator.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( CabAceInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_cab_ace (")
			.append("ace_id, acl_id,")
			.append("subject,subject_type,privilege,perm_json,")
			.append("modifier,last_modified")
			.append(")values(")
			.append("?,?,")
			.append("?,?,?,?,")
			.append("?,?)");
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				key.getId(),info.getAclId(),
				info.getSubject(),info.getSubjectType(),info.getPrivilege(),info.getPermissions(),
				info.getModifier(),info.getModifyDate()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		return jtemplate.update(SQL.toString(),params);

	}

	@Override
	public int delete( InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_cab_ace ")
			.append("where ace_id = ? ");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			id.getId()
		};
		
		int rtv = jtemplate.update(SQL.toString(), params);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		return rtv;
	}

	@Override
	public int update( CabAceInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_cab_ace set ")
		.append("acl_id = ?,")
		.append("subject = ?,subject_type = ?,privilege = ?,perm_json = ?,")
		.append("modifier = ?,last_modified = ? ")
		.append("where ace_id = ? ");
		
		Object[] params = new Object[]{
				info.getAclId(),
				info.getSubject(),info.getSubjectType(),info.getPrivilege(),info.getPermissions(),
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
	public CabAceInfo query( InfoId<?> id) {
		String SQL = "select * from gp_cab_ace "
				+ "where ace_id = ?";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		CabAceInfo ainfo = jtemplate.queryForObject(SQL, params, CabAceMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public static RowMapper<CabAceInfo> CabAceMapper = new RowMapper<CabAceInfo>(){

		@Override
		public CabAceInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			CabAceInfo info = new CabAceInfo();
			InfoId<Long> id = IdKey.CAB_ACE.getInfoId(rs.getLong("ace_id"));
			
			info.setInfoId(id);
			info.setAclId(rs.getLong("acl_id"));
			info.setSubject(rs.getString("subject"));
			info.setSubjectType(rs.getString("subject_type"));
			info.setPrivilege(rs.getInt("privilege"));
			info.setPermissions(rs.getString("perm_json"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}};
		


	@Override
	public RowMapper<CabAceInfo> getRowMapper() {
		
		return CabAceMapper;
	}

	@Override
	public CabAceInfo queryBySubject(Long aclid, String type, String subject) {
		
		StringBuffer qbuf = new StringBuffer("Select * from gp_cab_ace ")
				.append("where ace_id = ? and subject_type =? and subject = ?");
		
		Object[] params = new Object[]{
				aclid,
				type,
				subject
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		List<CabAceInfo> ainfo = jtemplate.query(qbuf.toString(), params, CabAceMapper);
		return CollectionUtils.isEmpty(ainfo) ? null: ainfo.get(0);
	}

	@Override
	public int deleteByAcl(Long aclid) {

		StringBuffer rbuf = new StringBuffer("Delete from gp_cab_ace where acl_id = ?");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		return jtemplate.update(rbuf.toString(), aclid);
	}

	@Override
	public List<CabAceInfo> queryByAcl(Long aclid) {
		
		StringBuffer qbuf = new StringBuffer("Select * from gp_cab_ace ")
				.append("where ace_id = ?");
		
		Object[] params = new Object[]{
				aclid,
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		List<CabAceInfo> ainfo = jtemplate.query(qbuf.toString(), params, CabAceMapper);
		return ainfo;
	}

	@Override
	public int deleteBySubject(Long aclid, String type, String subject) {

		StringBuffer rbuf = new StringBuffer("Delete from gp_cab_ace ")
				.append("where ace_id = ? and subject_type =? and subject = ?");
		Object[] params = new Object[]{
				aclid,
				type,
				subject
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		int cnt = jtemplate.update(rbuf.toString(), params, CabAceMapper);
		return cnt;
	}

}
