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
import com.gp.config.ServiceConfigurer;
import com.gp.dao.ShareDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.info.ShareInfo;

@Component("shareDAO")
public class ShareDAOImpl extends DAOSupport implements ShareDAO{

	Logger LOGGER = LoggerFactory.getLogger(ShareDAOImpl.class);
	
	@Autowired
	public ShareDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( ShareInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_shares (")
			.append("share_id,source_id,workgroup_id,")
			.append("sharer,target,owm,share_key,")
			.append("share_time,expire_time,access_limit,access_times,")
			.append("share_name, descr,")
			.append("modifier, last_modified")
			.append(")values(")
			.append("?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,")
			.append("?,?)");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				key.getId(),info.getSourceId(),info.getWorkgroupId(),
				info.getSharer(),info.getTarget(),info.getOwm(),info.getShareKey(),
				info.getShareDate(),info.getExpireDate(),info.getAccessLimit(),info.getAccessTimes(),
				info.getShareName(),info.getDescription(),
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
		SQL.append("delete from gp_shares ")
			.append("where share_id = ? ");
		
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
	public int update( ShareInfo info, FlatColLocator ...exclcols) {
		Set<String> cols = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
	
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_shares set ");
		
		if(!cols.contains("workgroup_id")){
			SQL.append("workgroup_id = ?,");
			params.add(info.getWorkgroupId());
		}
		if(!cols.contains("source_id")){
			SQL.append("source_id = ? ,");
			params.add(info.getWorkgroupId());
		}
		if(!cols.contains("sharer")){
			SQL.append("sharer = ? , ");
			params.add(info.getSharer());
		}
		if(!cols.contains("target")){
			SQL.append("target = ?, ");
			params.add(info.getTarget());
		}
		if(!cols.contains("owm")){
		SQL.append("owm = ?,");
		params.add(info.getOwm());
		}
		if(!cols.contains("share_key")){
			SQL.append("share_key = ?, ");
			params.add(info.getShareKey());
		}
		if(!cols.contains("share_time")){
			SQL.append("share_time = ?, ");
			params.add(info.getShareDate());
		}
		if(!cols.contains("expire_time")){
			SQL.append("expire_time = ?, ");
			params.add(info.getExpireDate());
		}
		if(!cols.contains("access_limit")){
			SQL.append("access_limit = ?,");
			params.add(info.getAccessLimit());
		}
		if(!cols.contains("share_name")){
			SQL.append("share_name = ?, ");
			params.add(info.getShareName());
		}
		if(!cols.contains("descr")){
			SQL.append("descr = ?,");
			params.add(info.getDescription());
		}
		
		SQL.append("access_times = ?, modifier = ?, last_modified = ? ")
			.append("where share_id = ? and ");
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
	public ShareInfo query( InfoId<?> id) {
		String SQL = "select * from gp_shares "
				+ "where share_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		List<ShareInfo> ainfo = jtemplate.query(SQL, params, ShareMapper);
		return ainfo.size()>0 ? ainfo.get(0):null;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);		
	}


}
