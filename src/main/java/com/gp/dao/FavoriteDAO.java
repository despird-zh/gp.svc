package com.gp.dao;

import java.util.List;
import java.util.Map;

import com.gp.info.FavoriteInfo;
import com.gp.info.InfoId;

public interface FavoriteDAO extends BaseDAO<FavoriteInfo>{

	public Map<InfoId<Long>, Integer> querySummary(String resourceType,List<Long> ids);
	
	public List<FavoriteInfo> queryByAccount(String type, String favoriter);
	
	public int delete(String favorite, InfoId<Long> resourceId);
}
