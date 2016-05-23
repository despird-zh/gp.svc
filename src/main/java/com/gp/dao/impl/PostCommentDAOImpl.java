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
import com.gp.dao.PostCommentDAO;
import com.gp.info.InfoId;
import com.gp.info.PostCommentInfo;

@Component("postCommentDAO")
public class PostCommentDAOImpl extends DAOSupport implements PostCommentDAO{

	Logger LOGGER = LoggerFactory.getLogger(PostCommentDAOImpl.class);
	
	@Autowired
	public PostCommentDAOImpl(@Qualifier(ServiceConfigurator.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( PostCommentInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_post_comments (")
			.append("source_id,workgroup_id,comment_pid,owm,")
			.append("comment_id,post_id,hash_code,author,")
			.append("owner,content,state,comment_time,")
			.append("modifier,last_modified")
			.append(")values(")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?)");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				info.getSourceId(),info.getWorkgroupId(),info.getParentId(),info.getOwm(),
				key.getId(),info.getPostId(),info.getHashCode(),info.getAuthor(),
				info.getOwner(),info.getContent(),info.getState(),info.getCommentDate(),
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
		SQL.append("delete from gp_post_comments ")
			.append("where comment_id = ? ");
		
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
	public int update( PostCommentInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_post_comments set ")
		.append("workgroup_id = ?,comment_pid = ?,owm = ?,")
		.append("post_id = ?,hash_code = ?,author = ?,source_id = ? ,")
		.append("owner = ?,content = ?,state = ?,comment_time = ?,")
		.append("modifier = ?,last_modified = ? ")
		.append("where comment_id = ? ");
		
		Object[] params = new Object[]{
				info.getWorkgroupId(),info.getParentId(),info.getOwm(),
				info.getPostId(),info.getHashCode(),info.getAuthor(),info.getSourceId(),
				info.getOwner(),info.getContent(),info.getState(),info.getCommentDate(),
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
	public PostCommentInfo query( InfoId<?> id) {
		String SQL = "select * from gp_post_comments "
				+ "where comment_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		List<PostCommentInfo> ainfo = jtemplate.query(SQL, params, PostCommentMapper);
		return ainfo.size()>0 ? ainfo.get(0):null;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public static RowMapper<PostCommentInfo> PostCommentMapper = new RowMapper<PostCommentInfo>(){

		@Override
		public PostCommentInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			PostCommentInfo info = new PostCommentInfo();
			InfoId<Long> id = IdKey.POST_COMMENT.getInfoId(rs.getLong("comment_id"));
			info.setInfoId(id);
			info.setSourceId(rs.getInt("source_id"));
			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setParentId(rs.getLong("comment_pid"));
			info.setPostId(rs.getLong("post_id"));
			info.setHashCode(rs.getString("hash_code"));
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
	public RowMapper<PostCommentInfo> getRowMapper() {
		// TODO Auto-generated method stub
		return PostCommentMapper;
	}
}
