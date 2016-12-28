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
import com.gp.dao.RoleDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.dao.info.RoleInfo;

@Component
public class RoleDAOImpl extends DAOSupport implements RoleDAO{

	static Logger LOGGER = LoggerFactory.getLogger(TagDAOImpl.class);
	
	@Autowired
	public RoleDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create(RoleInfo info) {
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_roles (")
			.append("role_id,role_name,descr,")
			.append("modifier, last_modified")
			.append(")values(")
			.append("?,?,?,")
			.append("?,?)");
		
		InfoId<Integer> key = info.getInfoId();
		Object[] params = new Object[]{
				key.getId(),info.getRoleName(),info.getDescription(),
				info.getModifier(),info.getModifyDate()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		return jtemplate.update(SQL.toString(),params);
	}

	@Override
	public int delete(InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_roles ")
			.append("where role_id = ? ");
		
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
	public int update(RoleInfo info,FilterMode mode, FlatColLocator ...exclcols) {
		Set<String> colset = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		
		SQL.append("update gp_roles set ");
		if(columnCheck(mode, colset, "role_name")){
			SQL.append("role_name = ?,");
			params.add(info.getRoleName());
		}
		if(columnCheck(mode, colset, "descr")){
			SQL.append("descr = ?,");
			params.add(info.getDescription());
		}
		SQL.append("modifier = ?, last_modified = ? ");
		SQL.append("where role_id = ? ");
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
	public RoleInfo query(InfoId<?> id) {
		
		String SQL = "select * from gp_roles "
				+ "where role_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		
		List<RoleInfo> rinfos = jtemplate.query(SQL, params, ROLE_Mapper);
		
		return CollectionUtils.isEmpty(rinfos)?null : rinfos.get(0);
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<RoleInfo> queryAll() {
		
		String SQL = "select * from gp_roles ";
	
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : {}" + SQL );
		}
		
		List<RoleInfo> rinfos = jtemplate.query(SQL, ROLE_Mapper);
		
		return rinfos;
	}

}
