package com.gp.svc;

import java.util.List;
import java.util.Map;

import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.dao.info.FavoriteInfo;
import com.gp.info.InfoId;

public interface FavoriteService {

	public Map<InfoId<Long>, Integer> getFavFileSummary(ServiceContext svcctx,  List<InfoId<Long>>infoids )throws ServiceException;
	
	public Map<InfoId<Long>, Integer> getFavFolderSummary(ServiceContext svcctx, List<InfoId<Long>>infoids )throws ServiceException;
	
	public List<FavoriteInfo> getFavorites(ServiceContext svcctx,String type, String favoriter)throws ServiceException;
	
	public boolean addFavorite(ServiceContext svcctx, FavoriteInfo fav)throws ServiceException;
	
	public boolean removeFavorite(ServiceContext svcctx, String favoriter, InfoId<Long> resourceId)throws ServiceException;
}
