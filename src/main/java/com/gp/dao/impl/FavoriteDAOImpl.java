package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.FavoriteDAO;
import com.gp.info.FavoriteInfo;
import com.gp.info.InfoId;

@Component("favoriteDAO")
public class FavoriteDAOImpl extends DAOSupport implements FavoriteDAO{

	Logger LOGGER = LoggerFactory.getLogger(FavoriteDAOImpl.class);
	
	@Autowired
	public FavoriteDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create(FavoriteInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_favorites (")
			.append("favorite_id,favoriter,resource_id, resource_type,")
			.append("modifier, last_modified")
			.append(")values(")
			.append("?,?,?,?,")
			.append("?,?)");
		
		Object[] params = new Object[]{
				info.getInfoId().getId(),info.getFavoriter(),info.getResourceId(),info.getResourceType(),
				info.getModifier(),info.getModifyDate()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		return jtemplate.update(SQL.toString(),params);
	}

	@Override
	public int delete(InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_favorites ")
			.append("where favorite_id = ?");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			id.getId()
		};
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(), params);
		return rtv;
	}

	@Override
	public int update(FavoriteInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_favorites set ")
			.append("favoriter = ? ,resource_id = ?, resource_type = ?,")
			.append("modifier = ?, last_modified = ? ")
			.append("where favorite_id = ?");
		
		Object[] params = new Object[]{
				info.getFavoriter(),info.getResourceId(),info.getResourceType(),
				info.getModifier(),info.getModifyDate(),info.getInfoId().getId()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(),params);
		return rtv;
	}

	@Override
	public FavoriteInfo query(InfoId<?> id) {
		String SQL = "select * from gp_favorites "
				+ "where favorite_id = ?";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		List<FavoriteInfo> infos = jtemplate.query(SQL, params, FavMapper);
		return CollectionUtils.isEmpty(infos)? null : infos.get(0);
	}

	@Override
	public RowMapper<FavoriteInfo> getRowMapper() {
	
		return FavMapper;
	}

	public static RowMapper<FavoriteInfo> FavMapper = new RowMapper<FavoriteInfo>(){

		@Override
		public FavoriteInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			FavoriteInfo info = new FavoriteInfo();
			InfoId<Long> id = IdKey.FAVORITE.getInfoId(rs.getLong("favorite_id"));
			info.setInfoId(id);
			info.setFavoriter(rs.getString("favoriter"));
			info.setResourceId(rs.getLong("resource_id"));
			info.setResourceType(rs.getString("resource_type"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};
	
	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Map<InfoId<Long>, Integer> querySummary(String resourceType,List<Long> ids) {
		
		final Map<InfoId<Long>, Integer> rtv = new HashMap<InfoId<Long>, Integer>();
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("select count(favorite_id) as fav_count ,resource_id,resource_type ");
		SQL.append("from gp_favorites ");
		SQL.append("where resource_type = :res_type ");
		SQL.append("AND resource_id in (:res_ids ) ");
		SQL.append("group by resource_id, resource_type ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("res_type", resourceType);
		
		params.put("res_ids", ids);
		NamedParameterJdbcTemplate jtemplate = this.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + params.toString());
		}
		
		jtemplate.query(SQL.toString(),params, new RowCallbackHandler(){

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				Long id = rs.getLong("resource_id");
				String type = rs.getString("resource_type");
				InfoId<Long> rid = IdKey.valueOfIgnoreCase(type).getInfoId(id);
				
				Integer count = rs.getInt("fav_count");
				
				rtv.put(rid, count);
			}});
		
		return rtv;
	}

	@Override
	public List<FavoriteInfo> queryByAccount(String type, String favoriter) {
		StringBuffer SQL = new StringBuffer("select * from gp_favorites ");
		SQL.append("where favoriter = ? ");
		
		List<Object> params = new ArrayList<Object>();
		params.add(favoriter);
		if(StringUtils.isNotBlank(type)){
			SQL.append("and resource_type = ?");
			params.add(type);
		}
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		List<FavoriteInfo> infos = jtemplate.query(SQL.toString(), params.toArray(), FavMapper);
		return infos;
	}

	@Override
	public int delete(String favoriter, InfoId<Long> resourceId) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_favorites ")
			.append("where resource_id = ? and resource_type = ? and favoriter = ?");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
				resourceId.getId(), resourceId.getIdKey(), favoriter
		};
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(), params);
		return rtv;
	}

}
