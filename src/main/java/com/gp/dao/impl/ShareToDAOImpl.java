package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.ShareToDAO;
import com.gp.info.InfoId;
import com.gp.info.ShareToInfo;

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
	public int update(ShareToInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_share_to set ")
			.append("workgroup_id = ?,share_id = ?,source_id = ?, ")
			.append("share_name = ?,to_account = ?,to_global_account = ?,to_email = ?,")
			.append("share_mode = ?,owm = ?,share_token = ?,access_count = ?,")
			.append("modifier = ?, last_modified = ?")
			.append("where share_to_id = ? ");
		
		Object[] params = new Object[]{
				info.getWorkgroupId(),info.getShareId(),info.getSourceId(),
				info.getShareName(),info.getToAccount(),info.getToGlobalAccount(),info.getToEmail(),
				info.getShareMode(),info.getOwm(),info.getShareToken(),info.getAccessCount(),
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
	public RowMapper<ShareToInfo> getRowMapper() {
		
		return ShareToMapper;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public static RowMapper<ShareToInfo> ShareToMapper = new RowMapper<ShareToInfo>(){

		@Override
		public ShareToInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			ShareToInfo info = new ShareToInfo();
			
			InfoId<Long> id = IdKey.SHARE_TO.getInfoId(rs.getLong("share_to_id"));
			
			info.setInfoId(id);
			info.setSourceId(rs.getInt("source_id"));
			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setShareId(rs.getLong("share_id"));
			info.setShareName(rs.getString("share_name"));
			info.setToAccount(rs.getString("to_account"));
			info.setToGlobalAccount(rs.getString("to_global_account"));
			info.setToEmail(rs.getString("to_email"));
			info.setShareMode(rs.getString("share_mode"));
			info.setOwm(rs.getLong("owm"));
			info.setShareToken(rs.getString("share_token"));
			info.setAccessCount(rs.getInt("access_count"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};
}
