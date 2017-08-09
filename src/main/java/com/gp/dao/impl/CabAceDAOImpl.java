package com.gp.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
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
import com.gp.dao.CabAceDAO;
import com.gp.dao.info.CabAceInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;

@Component
public class CabAceDAOImpl extends DAOSupport implements CabAceDAO{

	static Logger LOGGER = LoggerFactory.getLogger(CabAceDAOImpl.class);
	
	@Autowired
	public CabAceDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( CabAceInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_cab_ace (")
			.append("ace_id, acl_id, browse, ")
			.append("subject,subject_type,privilege_json,perm_json,")
			.append("modifier,last_modified")
			.append(")values(")
			.append("?,?,?,")
			.append("?,?,?,?,")
			.append("?,?)");
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				key.getId(),info.getAclId(),info.getBrowse(),
				info.getSubject(),info.getSubjectType(),info.getPrivileges(),info.getPermissions(),
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
	public int update( CabAceInfo info,FilterMode mode, FlatColLocator ...exclcols) {
		Set<String> colset = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_cab_ace set ");
		
		if(columnCheck(mode, colset, "acl_id")){
		SQL.append("acl_id = ?,");
		params.add(info.getAclId());
		}
		if(columnCheck(mode, colset, "browse")){
		SQL.append("browse = ?,");
		params.add(info.getBrowse());
		}
		if(columnCheck(mode, colset, "subject")){
		SQL.append("subject = ?,");
		params.add(info.getSubject());
		}
		if(columnCheck(mode, colset, "subject_type")){
		SQL.append("subject_type = ?,");
		params.add(info.getSubjectType());
		}
		if(columnCheck(mode, colset, "privilege_json")){
		SQL.append("privilege_json = ?,");
		params.add(info.getPrivileges());
		}
		if(columnCheck(mode, colset, "perm_json")){
			SQL.append("perm_json = ?,");
			params.add(info.getPermissions());
		}
		
		SQL.append("modifier = ?,last_modified = ? ");
		SQL.append("where ace_id = ? ");
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
