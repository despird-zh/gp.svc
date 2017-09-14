package com.gp.svc.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.gp.common.ServiceContext;
import com.gp.dao.SyncMsgOutDAO;
import com.gp.dao.info.SyncMsgOutInfo;
import com.gp.exception.ServiceException;
import com.gp.svc.SyncService;

@Service
public class SyncServiceImpl implements SyncService{

	Logger LOGGER = LoggerFactory.getLogger(SyncServiceImpl.class);

	@Autowired
	SyncMsgOutDAO msgOutDao;
	
	@Override
	public boolean newSyncMsgOut(ServiceContext svcctx, SyncMsgOutInfo syncMsgOut) throws ServiceException {
		
		try {
			svcctx.setTraceInfo(syncMsgOut);
			return msgOutDao.create(syncMsgOut) > 0;
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.create",dae, "SyncMsgOut");
		}
	}
}
