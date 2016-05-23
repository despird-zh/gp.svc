package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.config.ServiceConfigurator;
import com.gp.dao.TagDAO;
import com.gp.info.InfoId;
import com.gp.info.TagInfo;

@Component("tagDAO")
public class TagDAOImpl extends DAOSupport implements TagDAO{

	static Logger LOGGER = LoggerFactory.getLogger(TagDAOImpl.class);
	
	@Autowired
	public TagDAOImpl(@Qualifier(ServiceConfigurator.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( TagInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_tags (")
			.append("tag_id,tag_name,")
			.append("category,tag_type,")
			.append("modifier, last_modified")
			.append(")values(")
			.append("?,?,")
			.append("?,?,")
			.append("?,?)");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				key.getId(),info.getTagName(),
				info.getCategory(),info.getTagType(),
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
		SQL.append("delete from gp_tags ")
			.append("where tag_id = ? ");
		
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
	public int update( TagInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_tags set ")
			.append("tag_name = ?,")
			.append("category = ?,tag_type = ? , ")
			.append("modifier = ?, last_modified = ? ")
			.append("where tag_id = ? ");
		
		Object[] params = new Object[]{
				info.getTagName(),
				info.getCategory(),info.getTagType(),
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
	public TagInfo query( InfoId<?> id) {
		String SQL = "select * from gp_tags "
				+ "where tag_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		TagInfo ainfo = jtemplate.queryForObject(SQL, params, TagMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public static RowMapper<TagInfo> TagMapper = new RowMapper<TagInfo>(){

		@Override
		public TagInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			TagInfo info = new TagInfo();
			InfoId<Long> id = IdKey.TAG.getInfoId(	rs.getLong("tag_id"));
			info.setInfoId(id);

			info.setTagName(rs.getString("tag_name"));
			info.setCategory(rs.getString("category"));
			info.setTagType(rs.getString("tag_type"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};

	@Override
	public RowMapper<TagInfo> getRowMapper() {
		
		return TagMapper;
	}

	@Override
	public List<TagInfo> queryTags(String tagType, String category, String tagName) {
		
		List<TagInfo> result = null;
		ArrayList<String> paramlist = new ArrayList<String>();
		StringBuffer SQL = new StringBuffer("SELECT * FROM gp_tags ");
		SQL.append("WHERE tag_type = ? AND ");
		paramlist.add(tagType);
		if(StringUtils.isNotBlank(category)){
			SQL.append(" category = ? AND ");
			paramlist.add(category);
		}
		SQL.append(" tag_name LIKE ?");
		paramlist.add(StringUtils.trim(tagName)+"%");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : {} / PARAM : {}", SQL.toString(), ArrayUtils.toString(paramlist));
		}
		result = jtemplate.query(SQL.toString(), paramlist.toArray(), TagMapper);
		
		return result;
	}

	@Override
	public int deleteTag(String tagType, String category, String tagName) {
		ArrayList<String> paramlist = new ArrayList<String>();
		StringBuffer SQL = new StringBuffer("DELETE FROM gp_tags ");
		SQL.append("WHERE tag_type = ? AND ");
		paramlist.add(tagType);
		if(StringUtils.isNotBlank(category)){
			SQL.append(" category = ? AND ");
			paramlist.add(category);
		}
		SQL.append(" tag_name = ?");
		paramlist.add(StringUtils.trim(tagName));
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : {} / PARAM : {}", SQL.toString(), ArrayUtils.toString(paramlist));
		}
		int cnt = jtemplate.update(SQL.toString(), paramlist.toArray());
		return cnt;
	}
}
