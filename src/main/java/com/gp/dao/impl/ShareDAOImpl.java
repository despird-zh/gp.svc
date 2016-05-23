package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
import com.gp.dao.ShareDAO;
import com.gp.info.InfoId;
import com.gp.info.ShareInfo;

@Component("shareDAO")
public class ShareDAOImpl extends DAOSupport implements ShareDAO{

	Logger LOGGER = LoggerFactory.getLogger(ShareDAOImpl.class);
	
	@Autowired
	public ShareDAOImpl(@Qualifier(ServiceConfigurator.DATA_SRC)DataSource dataSource) {
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
	public int update( ShareInfo info) {
	
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_shares set ")
			.append("workgroup_id = ?,source_id = ? ,")
			.append("sharer = ? , target = ?, owm = ?,")
			.append("share_key = ?, share_time = ?, expire_time = ?, access_limit = ?,")
			.append("share_name = ?, descr = ?,")
			.append("access_times = ?, modifier = ?, last_modified = ? ")
			.append("where share_id = ? and ");
		
		Object[] params = new Object[]{
				info.getWorkgroupId(),info.getSourceId(),
				info.getSharer(),info.getTarget(),info.getOwm(),
				info.getShareKey(),info.getShareDate(),info.getExpireDate(),info.getAccessLimit(),
				info.getShareName(),info.getDescription(),
				info.getAccessTimes(),info.getModifier(),info.getModifyDate(),
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

	public static RowMapper<ShareInfo> ShareMapper = new RowMapper<ShareInfo>(){

		@Override
		public ShareInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			ShareInfo info = new ShareInfo();
			
			InfoId<Long> id = IdKey.SHARE.getInfoId(rs.getLong("share_id"));
			info.setInfoId(id);
			
			info.setSourceId(rs.getInt("source_id"));
			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setSharer(rs.getString("sharer"));
			info.setTarget(rs.getString("target"));
			info.setOwm(rs.getLong("owm"));
			info.setShareKey(rs.getString("share_key"));
			info.setShareDate(rs.getTimestamp("share_time"));
			info.setExpireDate(rs.getTimestamp("expire_time"));
			info.setAccessLimit(rs.getInt("access_limit"));
			info.setAccessTimes(rs.getInt("access_times"));
			info.setShareName(rs.getString("share_name"));
			info.setDescription(rs.getString("descr"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};

	@Override
	public RowMapper<ShareInfo> getRowMapper() {
		// TODO Auto-generated method stub
		return ShareMapper;
	}
}
