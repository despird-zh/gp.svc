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
import com.gp.common.DataSourceHolder;
import com.gp.dao.TagRelDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.dao.info.TagRelInfo;

@Component
public class TagRelDAOImpl extends DAOSupport implements TagRelDAO{
	static Logger LOGGER = LoggerFactory.getLogger(TagRelDAOImpl.class);
	
	@Autowired
	public TagRelDAOImpl(@Qualifier(DataSourceHolder.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( TagRelInfo info) {
				
		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_tag_rel (")
			.append("rel_id,")
			.append("resource_id,resource_type,tag_name,category,")
			.append("modifier, last_modified")
			.append(")values(")
			.append("?,")
			.append("?,?,?,?,")
			.append("?,?)");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				key.getId(),
				info.getResourceId(),info.getResourceType(),info.getTagName(),info.getCategory(),
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
		SQL.append("delete from gp_tag_rel ")
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
	public int update( TagRelInfo info, FilterMode mode, FlatColLocator ...exclcols) {
		Set<String> colset = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_tag_rel set ");
		
		if(columnCheck(mode, colset, "resource_id")){
			SQL.append("resource_id = ?,");
			params.add(info.getResourceId());
		}
		if(columnCheck(mode, colset, "resource_type")){
			SQL.append("resource_type = ? ,");
			params.add(info.getResourceType());
		}
		if(columnCheck(mode, colset, "tag_name")){
			SQL.append("tag_name = ?, ");
			params.add(info.getTagName());
		}
		if(columnCheck(mode, colset, "category")){
			SQL.append("category = ?,");
			params.add(info.getCategory());
		}
		
		SQL.append("modifier = ?, last_modified = ? ")
			.append("where rel_id = ?");
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
	public TagRelInfo query( InfoId<?> id) {
		String SQL = "select * from gp_tag_rel "
				+ "where rel_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		TagRelInfo ainfo = jtemplate.queryForObject(SQL, params, TagRelMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}


	@Override
	public TagRelInfo query( InfoId<?> resId, String tagName) {
		String SQL = "select * from gp_tag_rel "
				+ "where resource_id = ? and resource_type = ? and tag_name = ?";
		
		Object[] params = new Object[]{				
				resId.getId(), resId.getIdKey(), tagName
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		List<TagRelInfo> infos = jtemplate.query(SQL, params, TagRelMapper);
		return CollectionUtils.isEmpty(infos)? null : infos.get(0);
	}
	
	@Override
	public int delete(InfoId<?> resId, String tagName) {
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_tag_rel ")
			.append("where resource_id = ? and resource_type = ? and tag_name = ?");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			resId.getId(), resId.getIdKey(), tagName
		};
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(), params);
		return rtv;
	}
}
