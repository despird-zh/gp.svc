package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.dao.CabCommentDAO;
import com.gp.info.CabCommentInfo;
import com.gp.info.InfoId;

@Component("cabCommentDAO")
public class CabCommentDAOImpl extends DAOSupport implements CabCommentDAO{
	
	Logger LOGGER = LoggerFactory.getLogger(CabCommentDAOImpl.class);
	
	@Autowired
	public CabCommentDAOImpl(DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( CabCommentInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_cab_comments (")
			.append("source_id,workgroup_id,comment_pid,hash_code,")
			.append("comment_id,file_id,author,owm,")
			.append("owner,content,state,comment_time,")
			.append("modifier,last_modified")
			.append(")values(")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?)");
		
		InfoId<Long> key = info.getInfoId();
		
		Object[] params = new Object[]{
				info.getSourceId(),info.getWorkgroupId(),info.getParentId(),info.getHashCode(),
				key.getId(),info.getDocId(),info.getAuthor(),info.getOwm(),
				info.getOwner(),info.getContent(),info.getState(),info.getCommentDate(),
				info.getModifier(),info.getModifyDate()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		return jtemplate.update(SQL.toString(),params);

	}

	@Override
	public int delete( InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_cab_comments ")
			.append("where comment_id = ? ");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			id.getId()
		};
		
		int rtv = jtemplate.update(SQL.toString(), params);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		return rtv;
	}

	@Override
	public int update(CabCommentInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_cab_comments set ")
		.append("workgroup_id = ?,comment_pid = ?,hash_code = ?,")
		.append("file_id = ?,author = ?,owm = ?,source_id = ? ,")
		.append("owner = ?,content = ?,state = ?,comment_time = ?,")
		.append("modifier = ?,last_modified = ? ")
		.append("where comment_id = ? ");
		
		Object[] params = new Object[]{
				info.getWorkgroupId(),info.getParentId(),info.getHashCode(),
				info.getDocId(),info.getAuthor(),info.getOwm(),info.getSourceId(),
				info.getOwner(),info.getContent(),info.getState(),info.getCommentDate(),
				info.getModifier(),info.getModifyDate(),
				info.getInfoId().getId()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(),params);
		return rtv;
	}

	@Override
	public CabCommentInfo query( InfoId<?> id) {
		String SQL = "select * from gp_cab_comments "
				+ "where comment_id = ?";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		List<CabCommentInfo> ainfo = jtemplate.query(SQL, params, CabCommentMapper);
		return ainfo.size()>0 ? ainfo.get(0):null;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);		
	}

	public static RowMapper<CabCommentInfo> CabCommentMapper = new RowMapper<CabCommentInfo>(){

		@Override
		public CabCommentInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			CabCommentInfo info = new CabCommentInfo();
			
			InfoId<Long> id = IdKey.CAB_COMMENT.getInfoId(	rs.getLong("comment_id"));
			info.setInfoId(id);
			
			info.setSourceId(rs.getInt("source_id"));
			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setParentId(rs.getLong("comment_pid"));
			info.setDocId(rs.getLong("file_id"));
			info.setAuthor(rs.getString("author"));
			info.setOwner(rs.getString("owner"));
			info.setContent(rs.getString("content"));
			info.setState(rs.getString("state"));
			info.setCommentDate(rs.getTimestamp("comment_time"));
			info.setOwm(rs.getLong("owm"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};


	@Override
	public RowMapper<CabCommentInfo> getRowMapper() {
		
		return CabCommentMapper;
	}
}
