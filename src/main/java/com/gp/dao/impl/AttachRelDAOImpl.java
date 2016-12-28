package com.gp.dao.impl;

import java.util.ArrayList;
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
import com.gp.dao.AttachRelDAO;
import com.gp.dao.info.AttachRelInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;

@Component
public class AttachRelDAOImpl extends DAOSupport implements AttachRelDAO{
	
	Logger LOGGER = LoggerFactory.getLogger(AttachRelDAOImpl.class);
	
	@Autowired
	public AttachRelDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( AttachRelInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_attach_rel (")
			.append("rel_id, workgroup_id,")
			.append("resource_id,resource_type,atta_id,atta_name,")
			.append("atta_type,modifier, last_modified")
			.append(")values(")
			.append("?,?,")
			.append("?,?,?,?,")
			.append("?,?,?)");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				key.getId(),info.getWorkgroupId(),
				info.getResourceId(),info.getResourceType(),info.getAttachId(),info.getAttachName(),
				info.getAttachType(),info.getModifier(),info.getModifyDate()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int cnt = jtemplate.update(SQL.toString(),params);
		return cnt;
	}

	@Override
	public int delete( InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_attach_rel ")
			.append("where rel_id = ?");
		
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
	public int update( AttachRelInfo info, FilterMode mode, FlatColLocator ...exclcols) {

		Set<String> colset = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_attach_rel set ");
		
		if(columnCheck(mode, colset, "workgroup_id")){
			SQL.append("workgroup_id = ?,");
			params.add(info.getWorkgroupId());
		}
		if(columnCheck(mode, colset, "resource_id")){
			SQL.append("resource_id =?,");
			params.add(info.getResourceId());
		}
		if(columnCheck(mode, colset, "resource_type")){
			SQL.append("resource_type = ?,");
			params.add(info.getResourceType());
		}
		if(columnCheck(mode, colset, "atta_id")){
			SQL.append("atta_id = ?, ");
			params.add(info.getAttachId());
		}
		if(columnCheck(mode, colset, "atta_name")){
			SQL.append("atta_name = ?, ");
			params.add(info.getAttachName());
		}
		if(columnCheck(mode, colset, "atta_type")){
			SQL.append("atta_type=?,");
			params.add(info.getAttachType());
		}
		SQL.append("modifier = ?, last_modified = ? ");
		SQL.append("where rel_id = ? ");
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
	public AttachRelInfo query( InfoId<?> id) {
		String SQL = "select * from gp_attach_rel "
				+ "where rel_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		AttachRelInfo ainfo = jtemplate.queryForObject(SQL, params, AttachRelMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
