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
import com.gp.common.DataSourceHolder;
import com.gp.dao.ShareItemDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.dao.info.ShareItemInfo;

@Component
public class ShareItemDAOImpl extends DAOSupport implements ShareItemDAO{

	static Logger LOGGER = LoggerFactory.getLogger(ShareItemDAOImpl.class);
	
	@Autowired
	public ShareItemDAOImpl(@Qualifier(DataSourceHolder.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( ShareItemInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_share_item (")
			.append("share_item_id, workgroup_id,")
			.append("share_id,cabinet_id,resource_id,resource_type,")
			.append("modifier, last_modified")
			.append(")values(")
			.append("?,?,")
			.append("?,?,?,?,")
			.append("?,?)");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				key.getId(),info.getWorkgroupId(),
				info.getShareId(),info.getCabinetId(),info.getResourceId(),info.getResourceType(),
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
		SQL.append("delete from gp_share_item ")
			.append("where share_item_id = ? ");
		
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
	public int update(ShareItemInfo info, FilterMode mode,FlatColLocator ...exclcols) {
		Set<String> colset = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();

		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_share_item set ");
		
		if(columnCheck(mode, colset, "workgroup_id")){
			SQL.append("workgroup_id = ?,");
			params.add(info.getWorkgroupId());
		}
		if(columnCheck(mode, colset, "share_id")){
			SQL.append("share_id =?,");
			params.add(info.getShareId());
		}
		if(columnCheck(mode, colset, "cabinet_id")){
			SQL.append("cabinet_id = ?,");
			params.add(info.getCabinetId());
		}
		if(columnCheck(mode, colset, "resource_id")){
			SQL.append("resource_id = ? , ");
			params.add(info.getResourceId());
		}
		if(columnCheck(mode, colset, "resource_type")){
			SQL.append("resource_type = ?,");
			params.add(info.getResourceType());
		}
		SQL.append("modifier = ?, last_modified = ? ")
			.append("where share_item_id = ? ");
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
	public ShareItemInfo query( InfoId<?> id) {
		String SQL = "select * from gp_share_item "
				+ "where share_item_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		ShareItemInfo ainfo = jtemplate.queryForObject(SQL, params, ShareItemMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}


}
