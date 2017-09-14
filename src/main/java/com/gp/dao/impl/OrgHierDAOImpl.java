package com.gp.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.gp.common.FlatColumns;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.common.DataSourceHolder;
import com.gp.dao.OrgHierDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.dao.info.OrgHierInfo;


@Component
public class OrgHierDAOImpl extends DAOSupport implements OrgHierDAO{

	Logger LOGGER = LoggerFactory.getLogger(OrgHierDAOImpl.class);
		
	@Autowired
	public OrgHierDAOImpl(@Qualifier(DataSourceHolder.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( OrgHierInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_org_hier (")
			.append("org_pid,org_id,mbr_group_id,admin,")
			.append("org_level,org_name,descr,manager,")
			.append("mbr_post_acpt, email, modifier, last_modified")
			.append(")values(")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,?)");
		
		InfoId<Long> key = info.getInfoId();
		
		Object[] params = new Object[]{
				info.getParentOrg(),key.getId(),info.getMemberGroupId(),info.getAdmin(),
				info.getLevel(),info.getOrgName(),info.getDescription(),info.getManager(),
				info.getPostAcceptable(),info.getEmail(),info.getModifier(),info.getModifyDate()
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
	public int update(OrgHierInfo info, FilterMode mode, FlatColLocator ...exclcols) {
		Set<String> colset = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_org_hier set ");
		if(columnCheck(mode, colset, "org_pid")){
			SQL.append("org_pid = ?,");
			params.add(info.getParentOrg());
		}
		if(columnCheck(mode, colset, "mbr_group_id")){
			SQL.append("mbr_group_id = ?,");
			params.add(info.getMemberGroupId());
		}
		if(columnCheck(mode, colset, "admin")){
			SQL.append("admin = ?,");
			params.add(info.getAdmin());
		}
		if(columnCheck(mode, colset, "org_level")){
			SQL.append("org_level = ? ,");
			params.add(info.getLevel());
		}
		if(columnCheck(mode, colset, "org_name")){
			SQL.append("org_name = ?,");
			params.add(info.getOrgName());
		}
		if(columnCheck(mode, colset, "manager")){
			SQL.append("manager = ?, ");
			params.add(info.getManager());
		}
		if(columnCheck(mode, colset, "email")){
			SQL.append("email = ?,");
			params.add(info.getEmail());
		}
		if(columnCheck(mode, colset, "descr")){
			SQL.append("descr = ?,");
			params.add(info.getDescription());
		}
		if(columnCheck(mode, colset, "mbr_post_acpt")){
			SQL.append("mbr_post_acpt = ?,");
			params.add(info.getPostAcceptable());
		}
			
		SQL.append("modifier = ?, last_modified = ? ")
			.append("where org_id = ? ");
		params.add(info.getModifier());
		params.add(info.getModifyDate());
		params.add(info.getInfoId().getId());
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(),params.toArray());
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

	@Override
	public List<OrgHierInfo> queryByIds(InfoId<?>... ids) {

		List<Long> oids = new ArrayList<Long>();
		
		for(InfoId<?> id : ids){
			oids.add((Long)id.getId());
		}
		StringBuffer SQL = new StringBuffer("SELECT * FROM gp_org_hier ");
		SQL.append("WHERE org_id IN (:org_ids) ");
		SQL.append("ORDER BY org_id asc");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("org_ids", oids);
		
		NamedParameterJdbcTemplate jtemplate = super.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + oids.toString());
		}
		
		return jtemplate.query(SQL.toString(), params, OrgHierMapper);
	}

	@Override
	public List<OrgHierInfo> querySubByIds(InfoId<?>... ids) {
		List<Long> oids = new ArrayList<Long>();
		
		for(InfoId<?> id : ids){
			oids.add((Long)id.getId());
		}
		StringBuffer SQL = new StringBuffer("SELECT * FROM gp_org_hier ");
		SQL.append("WHERE org_pid IN (:org_ids) ");
		SQL.append("ORDER BY org_id asc");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("org_ids", oids);
		
		NamedParameterJdbcTemplate jtemplate = super.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + oids.toString());
		}
		
		return jtemplate.query(SQL.toString(), params, OrgHierMapper);
	}
}
