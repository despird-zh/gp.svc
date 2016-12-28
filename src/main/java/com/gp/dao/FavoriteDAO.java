package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.dao.info.FavoriteInfo;
import com.gp.info.InfoId;

public interface FavoriteDAO extends BaseDAO<FavoriteInfo>{

	public Map<InfoId<Long>, Integer> querySummary(String resourceType,List<Long> ids);
	
	public List<FavoriteInfo> queryByAccount(String type, String favoriter);
	
	public int delete(String favoriter, InfoId<Long> resourceId);
	
	public List<FavoriteInfo> queryByResource(InfoId<?> resourceId);
	
	public FavoriteInfo query(String favoriter, InfoId<Long> resourceId);
	
	public int deleteByResource(InfoId<?> resourceId);
	
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
}
