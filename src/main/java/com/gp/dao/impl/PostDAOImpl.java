package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.FlatColumns;
import com.gp.common.IdKey;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.PostDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.info.PostInfo;

@Component("postDAO")
public class PostDAOImpl extends DAOSupport implements PostDAO{

	Logger LOGGER = LoggerFactory.getLogger(PostDAOImpl.class);
	
	@Autowired
	public PostDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( PostInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_posts (")
			.append("source_id,workgroup_id,post_id,hash_code,")
			.append("owner,content,excerpt,title,owm,")
			.append("state,comment_on,post_type,comment_count,")
			.append("upvote_count,downvote_count,post_time,")
			.append("modifier,last_modified")
			.append(")values(")
			.append("?,?,?,?,")
			.append("?,?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,")
			.append("?,?)");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				info.getSourceId(),info.getWorkgroupId(),key.getId(),info.getHashCode(),
				info.getOwner(),info.getContent(),info.getExcerpt(),info.getTitle(),info.getOwm(),
				info.getState(),info.isCommentOn(),info.getPostType(),info.getCommentCount(),
				info.getUpvoteCount(),info.getDownvoteCount(),info.getPostDate(),
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
		SQL.append("delete from gp_posts ")
			.append("where post_id = ?");
		
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
	public int update( PostInfo info, FlatColLocator ...exclcols) {
		Set<String> cols = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_posts set ");
		
		if(!cols.contains("workgroup_id")){
			SQL.append("workgroup_id = ?,");
			params.add(info.getWorkgroupId());
		}
		if(!cols.contains("hash_code")){
			SQL.append("hash_code = ?, ");
			params.add(info.getHashCode());
		}
		if(!cols.contains("source_id")){
			SQL.append("source_id = ?, ");
			params.add(info.getSourceId());
		}
		if(!cols.contains("owner")){
			SQL.append("owner = ? ,");
			params.add(info.getOwner());
		}
		if(!cols.contains("content")){
			SQL.append("content = ? ,");
			params.add(info.getContent());
		}
		if(!cols.contains("excerpt")){
			SQL.append("excerpt = ? ,");
			params.add(info.getExcerpt());
		}
		if(!cols.contains("title")){
			SQL.append("title = ? , ");
			params.add(info.getTitle());
		}
		if(!cols.contains("owm")){
			SQL.append("owm = ?,");
			params.add(info.getOwm());
		}
		if(!cols.contains("state")){
			SQL.append("state = ? ,");
			params.add(info.getState());
		}
		if(!cols.contains("comment_on")){
			SQL.append("comment_on = ? ,");
			params.add(info.isCommentOn());
		}
		if(!cols.contains("post_type")){
			SQL.append("post_type = ? ,");
			params.add(info.getPostType());
		}
		if(!cols.contains("comment_count")){
			SQL.append("comment_count = ? ,");
			params.add(info.getCommentCount());
		}
		if(!cols.contains("upvote_count")){
			SQL.append("upvote_count = ? ,");
			params.add(info.getUpvoteCount());
		}
		if(!cols.contains("downvote_count")){
			SQL.append("downvote_count = ? ,");
			params.add(info.getDownvoteCount());
		}
		if(!cols.contains("post_time")){
			SQL.append("post_time = ? ,");
			params.add(info.getPostDate());
		}
		
		SQL.append("modifier = ?,last_modified = ? ")
		.append("where post_id = ? ");
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
	public PostInfo query( InfoId<?> id) {
		String SQL = "select * from gp_posts "
				+ "where post_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		List<PostInfo> ainfo = jtemplate.query(SQL, params, PostMapper);
		return ainfo.size()>0 ? ainfo.get(0):null;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		
	}

	public static RowMapper<PostInfo> PostMapper = new RowMapper<PostInfo>(){

		@Override
		public PostInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			PostInfo info = new PostInfo();
			InfoId<Long> id = IdKey.POST.getInfoId(rs.getLong("post_id"));
			
			info.setInfoId(id);
			info.setSourceId(rs.getInt("source_id"));
			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setOwner(rs.getString("owner"));
			info.setContent(rs.getString("content"));
			info.setExcerpt(rs.getString("excerpt"));
			info.setTitle(rs.getString("title"));
			info.setState(rs.getString("state"));
			info.setCommentOn(rs.getBoolean("comment_on"));
			info.setPostType(rs.getString("post_type"));
			info.setCommentCount(rs.getInt("comment_count"));
			info.setUpvoteCount(rs.getInt("upvote_count"));
			info.setDownvoteCount(rs.getInt("downvote_count"));
			info.setPostDate(rs.getDate("post_time"));
			info.setHashCode(rs.getString("hash_code"));
			info.setOwm(rs.getLong("owm"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};

	@Override
	public RowMapper<PostInfo> getRowMapper() {
		// TODO Auto-generated method stub
		return PostMapper;
	}
}
