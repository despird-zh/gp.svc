package com.gp.dao.impl;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.dao.PostUserDAO;
import com.gp.info.InfoId;
import com.gp.info.PostUserInfo;

@Component("postUserDAO")
public class PostUserDAOImpl extends DAOSupport implements PostUserDAO{

	Logger LOGGER = LoggerFactory.getLogger(PostUserDAOImpl.class);
	
	@Autowired
	public PostUserDAOImpl(DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( PostUserInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_post_user (")
			.append("rel_id,source_id,global_account,")
			.append("workgroup_id,post_id,attendee,")
			.append("modifier, last_modified")
			.append(")values(")
			.append("?,?,?,")
			.append("?,?,?,")
			.append("?,?)");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				key.getId(),info.getSourceId(),info.getGlobalAccount(),
				info.getWorkgroupId(),info.getPostId(),info.getAttendee(),
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
		SQL.append("delete from gp_post_user ")
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
	public int update( PostUserInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_post_user set ")
			.append("workgroup_id = ?,global_account = ?,source_id = ? ,")
			.append("post_id = ? ,attendee = ?,")
			.append("modifier = ?, last_modified = ? ")
			.append("where rel_id = ? ");
		
		Object[] params = new Object[]{
				info.getWorkgroupId(),info.getGlobalAccount(),info.getSourceId(),
				info.getPostId(),info.getAttendee(),
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
	public PostUserInfo query( InfoId<?> id) {
		String SQL = "select * from gp_post_user "
				+ "where rel_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		List<PostUserInfo> ainfo = jtemplate.query(SQL, params, PostUserMapper);
		return ainfo.size() >0 ? ainfo.get(0):null;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public static RowMapper<PostUserInfo> PostUserMapper = new RowMapper<PostUserInfo>(){

		@Override
		public PostUserInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			PostUserInfo info = new PostUserInfo();
			InfoId<Long> id = IdKey.POST_USER.getInfoId(rs.getLong("rel_id"));
			
			info.setInfoId(id);
			
			info.setSourceId(rs.getInt("source_id"));
			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setPostId(rs.getLong("post_id"));
			info.setAttendee(rs.getString("attendee"));
			info.setGlobalAccount(rs.getString("global_account"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};

	@Override
	public RowMapper<PostUserInfo> getRowMapper() {
		
		return PostUserMapper;
	}
}
