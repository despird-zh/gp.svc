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
import com.gp.config.ServiceConfigurer;
import com.gp.dao.CabSumDAO;
import com.gp.info.CabSumInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;

@Component("cabinetsumDAO")
public class CabSumDAOImpl extends DAOSupport implements CabSumDAO{

	static Logger LOGGER = LoggerFactory.getLogger(CabSumDAOImpl.class);
	
	@Autowired
	public CabSumDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create(CabSumInfo info) {
		
		StringBuffer SQL = new StringBuffer();
		
		SQL.append("INSERT INTO gp_cab_summary (")
			.append("rel_id, cabinet_id, resource_id, resource_type,")
			.append("folder_sum, file_sum, total_size, modifier, last_modified")
			.append(")VALUES(")
			.append("?,?,?,?,")
			.append("?,?,?,?,?")
			.append(")");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				key.getId(),info.getCabinetId(), info.getResourceId(),info.getResourceType(),
				info.getFolderSummary(), info.getFileSummary(), info.getTotalSize(),info.getModifier(),info.getModifyDate()
		};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);

			// execute sql
		return jtemplate.update(SQL.toString(),params);
	}

	@Override
	public int delete(InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_cab_summary ")
			.append("where rel_id = ? ");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			id.getId()
		};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = -1;

		rtv = jtemplate.update(SQL.toString(), params);

		return rtv;
	}

	@Override
	public int update(CabSumInfo info, FlatColLocator... excludeCols) {
		Set<String> cols = FlatColumns.toColumnSet(excludeCols);
		List<Object> params = new ArrayList<Object>();

		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_cab_summary set ");
		
		if(!cols.contains("cabinet_id")){
			SQL.append("cabinet_id = ?,");
			params.add(info.getCabinetId());
		}
		if(!cols.contains("file_sum")){
			SQL.append("file_sum = ?,");
			params.add(info.getFileSummary());
		}
		if(!cols.contains("folder_sum")){
			SQL.append("folder_sum = ?,");
			params.add(info.getFolderSummary());
		}
		if(!cols.contains("total_size")){
			SQL.append("total_size = ?,");
			params.add(info.getTotalSize());
		}
		if(!cols.contains("resource_id")){
			SQL.append("resource_id = ?,");
			params.add(info.getResourceId());
		}
		if(!cols.contains("resource_type")){
			SQL.append("resource_type = ?,");
			params.add(info.getResourceType());
		}
		
		SQL.append("modifier = ?,last_modified = ? ")
		.append("where rel_id = ? ");
		params.add(info.getModifier());
		params.add(info.getModifyDate());
		params.add(info.getInfoId().getId());
		
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		int rtv = -1;

		rtv = jtemplate.update(SQL.toString(), params.toArray());

		return rtv;
	}

	@Override
	public CabSumInfo query(InfoId<?> id) {
		String SQL = "select * from gp_cab_summary "
				+ "where rel_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);

		List<CabSumInfo>	ainfo = jtemplate.query(SQL, params, CabSumMapper);

		return CollectionUtils.isEmpty(ainfo) ? null : ainfo.get(0);
	}

		
	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
