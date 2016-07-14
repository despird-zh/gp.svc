package com.gp.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.gp.common.FlatColumns;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.ShareToDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.dao.info.ShareToInfo;

@Component("shareToDAO")
public class ShareToDAOImpl extends DAOSupport implements ShareToDAO{

	@Autowired
	public ShareToDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create(ShareToInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_share_to (")
			.append("share_to_id,source_id,workgroup_id,share_id,")
			.append("share_name,to_account,to_global_account,to_email,")
			.append("share_mode,owm,share_token,access_count,")
			.append("modifier, last_modified")
			.append(")values(")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?)");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				key.getId(),info.getSourceId(),info.getWorkgroupId(),info.getShareId(),
				info.getShareName(),info.getToAccount(),info.getToGlobalAccount(),info.getToEmail(),
				info.getShareMode(),info.getOwm(),info.getShareToken(),info.getAccessCount(),
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
		SQL.append("delete from gp_share_to ")
			.append("where share_to_id = ? ");
		
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
	public int update(ShareToInfo info, FilterMode mode, FlatColLocator ...exclcols) {
		Set<String> colset = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_share_to set ");
		
		if(columnCheck(mode, colset, "workgroup_id")){
			SQL.append("workgroup_id = ?,");
			params.add(info.getWorkgroupId());
		}
		if(columnCheck(mode, colset, "share_id")){
			SQL.append("share_id = ?,");
			params.add(info.getShareId());
		}
		if(columnCheck(mode, colset, "source_id")){
			SQL.append("source_id = ?, ");
			params.add(info.getShareId());
		}
		if(columnCheck(mode, colset, "share_name")){
			SQL.append("share_name = ?,");
			params.add(info.getShareName());
		}
		if(columnCheck(mode, colset, "to_account")){
			SQL.append("to_account = ?,");
			params.add(info.getToAccount());
		}
		if(columnCheck(mode, colset, "to_global_account")){
			SQL.append("to_global_account = ?,");
			params.add(info.getToGlobalAccount());
		}
		if(columnCheck(mode, colset, "to_email")){
			SQL.append("to_email = ?,");
			params.add(info.getToEmail());
		}
		if(columnCheck(mode, colset, "share_mode")){
			SQL.append("share_mode = ?,");
			params.add(info.getShareMode());
		}
		if(columnCheck(mode, colset, "owm")){
			SQL.append("owm = ?,");
			params.add(info.getOwm());
		}
		if(columnCheck(mode, colset, "share_token")){
			SQL.append("share_token = ?,");
			params.add(info.getShareToken());
		}
		if(columnCheck(mode, colset, "access_count")){
			SQL.append("access_count = ?,");
			params.add(info.getAccessCount());
		}
		
		SQL.append("modifier = ?, last_modified = ?")
			.append("where share_to_id = ? ");
		
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
	public ShareToInfo query(InfoId<?> id) {
		String SQL = "select * from gp_share_to "
				+ "where share_to_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		List<ShareToInfo> ainfo = jtemplate.query(SQL, params, ShareToMapper);
		return ainfo.size()>0 ? ainfo.get(0):null;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
