package com.gp.dao.impl;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.gp.common.FlatColumns.FilterMode;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.CabAclDAO;
import com.gp.info.CabAclInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;

@Component("cabAclDAO")
public class CabAclDAOImpl extends DAOSupport implements CabAclDAO{

	Logger LOGGER = LoggerFactory.getLogger(CabAclDAOImpl.class);
	
	@Autowired
	public CabAclDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( CabAclInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_cab_acl (")
			.append("acl_id, acl_hash,")
			.append("modifier, last_modified")
			.append(")values(")
			.append("?,?,")
			.append("?,?)");
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				key.getId(),info.getAclHash(),				
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
		SQL.append("delete from gp_cab_acl ")
			.append("where acl_id = ? ");
		
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
	public int update(CabAclInfo info,FilterMode mode, FlatColLocator ...exclcols) {
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_cab_acl set ");
		
		SQL.append("acl_hash = ?,");
		
		SQL.append("modifier = ?,last_modified = ? ")
		.append("where acl_id = ? ");
		
		Object[] params = new Object[]{
				info.getAclHash(),				
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
	public CabAclInfo query( InfoId<?> id) {
		String SQL = "select * from gp_cab_acl "
				+ "where acl_id = ?";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		CabAclInfo ainfo = jtemplate.queryForObject(SQL, params, CabAclMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
