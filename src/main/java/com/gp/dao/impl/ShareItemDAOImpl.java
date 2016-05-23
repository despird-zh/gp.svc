package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.config.ServiceConfigurator;
import com.gp.dao.ShareItemDAO;
import com.gp.info.InfoId;
import com.gp.info.ShareItemInfo;

@Component("shareItemDAO")
public class ShareItemDAOImpl extends DAOSupport implements ShareItemDAO{

	static Logger LOGGER = LoggerFactory.getLogger(ShareItemDAOImpl.class);
	
	@Autowired
	public ShareItemDAOImpl(@Qualifier(ServiceConfigurator.DATA_SRC)DataSource dataSource) {
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
	public int update(ShareItemInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_share_item set ")
			.append("workgroup_id = ?,share_id =?,")
			.append("cabinet_id = ?,resource_id = ? , resource_type = ?,")
			.append("modifier = ?, last_modified = ? ")
			.append("where share_item_id = ? ");
		
		Object[] params = new Object[]{
				info.getWorkgroupId(),info.getShareId(),
				info.getCabinetId(),info.getResourceId(),info.getResourceType(),
				info.getModifier(),info.getModifyDate(),
				info.getInfoId().getId()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(),params);
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

	public static RowMapper<ShareItemInfo> ShareItemMapper = new RowMapper<ShareItemInfo>(){

		@Override
		public ShareItemInfo mapRow(ResultSet rs, int rowNum) throws SQLException {

			ShareItemInfo info = new ShareItemInfo();
			InfoId<Long> id = IdKey.SHARE_ITEM.getInfoId(rs.getLong("share_item_id"));
			info.setInfoId(id);

			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setShareId(rs.getLong("share_id"));
			info.setCabinetId(rs.getLong("cabinet_id"));
			info.setResourceId(rs.getLong("resource_id"));
			info.setResourceType(rs.getString("resource_type"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};

	@Override
	public RowMapper<ShareItemInfo> getRowMapper() {
		
		return ShareItemMapper;
	}
}
