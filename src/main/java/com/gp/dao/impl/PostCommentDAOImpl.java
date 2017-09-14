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
import com.gp.dao.PostCommentDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.dao.info.PostCommentInfo;

@Component
public class PostCommentDAOImpl extends DAOSupport implements PostCommentDAO{

	Logger LOGGER = LoggerFactory.getLogger(PostCommentDAOImpl.class);
	
	@Autowired
	public PostCommentDAOImpl(@Qualifier(DataSourceHolder.DATA_SRC)DataSource dataSource) {
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
	public int update( PostCommentInfo info, FilterMode mode,FlatColLocator ...exclcols) {
		Set<String> colset = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_post_comments set ");
		
		if(columnCheck(mode, colset, "workgroup_id")){
			SQL.append("workgroup_id = ?,");
			params.add(info.getWorkgroupId());
		}
		if(columnCheck(mode, colset, "comment_pid")){
			SQL.append("comment_pid = ?,");
			params.add(info.getParentId());
		}
		if(columnCheck(mode, colset, "owm")){
			SQL.append("owm = ?,");
			params.add(info.getOwm());
		}
		if(columnCheck(mode, colset, "post_id")){
			SQL.append("post_id = ?,");
			params.add(info.getPostId());
		}
		if(columnCheck(mode, colset, "hash_code")){
			SQL.append("hash_code = ?,");
			params.add(info.getHashCode());
		}
		if(columnCheck(mode, colset, "author")){
			SQL.append("author = ?,");
			params.add(info.getAuthor());
		}
		if(columnCheck(mode, colset, "source_id")){
			SQL.append("source_id = ? ,");
			params.add(info.getSourceId());
		}
		if(columnCheck(mode, colset, "owner")){
			SQL.append("owner = ?,");
			params.add(info.getOwner());
		}
		if(columnCheck(mode, colset, "content")){
			SQL.append("content = ?,");
			params.add(info.getContent());
		}
		if(columnCheck(mode, colset, "state")){
			SQL.append("state = ?,");
			params.add(info.getState());
		}
		if(columnCheck(mode, colset, "comment_time")){
			SQL.append("comment_time = ?,");
			params.add(info.getCommentDate());
		}
		
		SQL.append("modifier = ?,last_modified = ? ")
		.append("where comment_id = ? ");
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


}
