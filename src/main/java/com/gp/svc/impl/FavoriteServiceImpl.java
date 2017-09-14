package com.gp.svc.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.common.ServiceContext;
import com.gp.common.DataSourceHolder;
import com.gp.dao.FavoriteDAO;
import com.gp.dao.PseudoDAO;
import com.gp.exception.ServiceException;
import com.gp.dao.info.FavoriteInfo;
import com.gp.info.Identifier;
import com.gp.info.InfoId;
import com.gp.svc.FavoriteService;

@Service
public class FavoriteServiceImpl implements FavoriteService{

	@Autowired
	private FavoriteDAO favoritedao;
	
	@Autowired
	PseudoDAO pseudodao;
	
	@Transactional(value=DataSourceHolder.TRNS_MGR, readOnly=true)
	@Override
	public Map<InfoId<Long>, Integer> getFavFileSummary(ServiceContext svcctx,  List<InfoId<Long>>infoids )throws ServiceException{
		
		String type = IdKey.GP_CAB_FILES.getSchema();
		
		List<Long> ids = new ArrayList<Long>();
		
		for(InfoId<Long> infoid : infoids){
			ids.add(infoid.getId());
		}
		Map<InfoId<Long>, Integer> rtv = null;
		try{
			rtv = favoritedao.querySummary(type, ids);
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with",dae, "Favorite Files Summary", infoids.toString());
		}
		return rtv;

	}
	
	@Transactional(value=DataSourceHolder.TRNS_MGR, readOnly=true)
	@Override
	public Map<InfoId<Long>, Integer> getFavFolderSummary(ServiceContext svcctx, List<InfoId<Long>>infoids )throws ServiceException{
		
		String type = IdKey.GP_CAB_FOLDERS.getSchema();
		List<Long> ids = new ArrayList<Long>();
		for(InfoId<Long> infoid : infoids){
			ids.add(infoid.getId());
		}
		Map<InfoId<Long>, Integer> rtv = null;
		try{
			rtv = favoritedao.querySummary(type, ids);
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with",dae, "Favorite Folders Summary", infoids.toString());
		}
		return rtv;
	}
	
	@Transactional(value=DataSourceHolder.TRNS_MGR, readOnly=true)
	@Override
	public List<FavoriteInfo> getFavorites(ServiceContext svcctx,String type, String favoriter)throws ServiceException{
		
		List<FavoriteInfo> rtv = null;
		try{
			
			rtv = favoritedao.queryByAccount(type, favoriter);
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with",dae, "Favorite Summary", "favoriter:" + favoriter);
		}
		
		return rtv;
	}
	
	@Transactional(value=DataSourceHolder.TRNS_MGR)
	@Override
	public boolean addFavorite(ServiceContext svcctx, FavoriteInfo fav)throws ServiceException{
		
		boolean rtv = false;
		try{
			Identifier idf = IdKeys.valueOfIgnoreCase(fav.getResourceType());
			InfoId<Long> rid = new InfoId<Long>(idf, fav.getResourceId()); 
			FavoriteInfo favinfo = favoritedao.query(fav.getFavoriter(), rid);
			if(favinfo == null){
				svcctx.setTraceInfo(fav);
				rtv = favoritedao.create(fav) > 0;
			}
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.create", dae, "Favorite");
		}
		
		return rtv;
		
	}
	
	@Transactional(value=DataSourceHolder.TRNS_MGR)
	@Override
	public boolean removeFavorite(ServiceContext svcctx, String favoriter, InfoId<Long> resourceId)throws ServiceException{
		
		boolean rtv = false;
		try{
			
			rtv = favoritedao.delete(favoriter, resourceId) > 0;
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.delete.with", dae, favoriter + "'s Favorite", resourceId);
		}
		
		return rtv;
	}
}
