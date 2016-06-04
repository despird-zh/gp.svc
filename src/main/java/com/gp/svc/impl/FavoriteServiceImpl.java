package com.gp.svc.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.gp.common.IdKey;
import com.gp.common.ServiceContext;
import com.gp.dao.FavoriteDAO;
import com.gp.dao.PseudoDAO;
import com.gp.exception.ServiceException;
import com.gp.info.FavoriteInfo;
import com.gp.info.InfoId;
import com.gp.svc.FavoriteService;

@Service("favoriteService")
public class FavoriteServiceImpl implements FavoriteService{

	@Autowired
	private FavoriteDAO favoritedao;
	
	@Autowired
	PseudoDAO pseudodao;
	
	public Map<InfoId<Long>, Integer> getFavFileSummary(ServiceContext<?> svcctx,  List<InfoId<Long>>infoids )throws ServiceException{
		
		String type = IdKey.CAB_FILE.getSchema();
		List<Long> ids = new ArrayList<Long>();
		for(InfoId<Long> infoid : infoids){
			ids.add(infoid.getId());
		}
		Map<InfoId<Long>, Integer> rtv = null;
		try{
			rtv = favoritedao.querySummary(type, ids);
		}catch(DataAccessException dae){
			
			new ServiceException("Fail to query the resources fav summary ",dae);
		}
		return rtv;

	}
	
	public Map<InfoId<Long>, Integer> getFavFolderSummary(ServiceContext<?> svcctx, List<InfoId<Long>>infoids )throws ServiceException{
		
		String type = IdKey.CAB_FOLDER.getSchema();
		List<Long> ids = new ArrayList<Long>();
		for(InfoId<Long> infoid : infoids){
			ids.add(infoid.getId());
		}
		Map<InfoId<Long>, Integer> rtv = null;
		try{
			rtv = favoritedao.querySummary(type, ids);
		}catch(DataAccessException dae){
			
			new ServiceException("Fail to query the resources fav summary ",dae);
		}
		return rtv;
	}
	
	public List<FavoriteInfo> getFavorites(ServiceContext<?> svcctx,String type, String favoriter)throws ServiceException{
		
		List<FavoriteInfo> rtv = null;
		try{
			
			rtv = favoritedao.queryByAccount(type, favoriter);
		}catch(DataAccessException dae){
			
			new ServiceException("Fail to query the resources fav summary ",dae);
		}
		
		return rtv;
	}
	
	public boolean addFavorite(ServiceContext<?> svcctx, FavoriteInfo fav)throws ServiceException{
		
		boolean rtv = false;
		try{
			
			rtv = favoritedao.create(fav)>0;
		}catch(DataAccessException dae){
			
			new ServiceException("Fail to query the resources fav summary ",dae);
		}
		
		return rtv;
		
	}
	
	public boolean removeFavorite(ServiceContext<?> svcctx, String favoriter, InfoId<Long> resourceId)throws ServiceException{
		
		boolean rtv = false;
		try{
			
			rtv = favoritedao.delete(favoriter, resourceId) > 0;
			
		}catch(DataAccessException dae){
			
			new ServiceException("Fail to query the resources fav summary ",dae);
		}
		
		return rtv;
	}
}
