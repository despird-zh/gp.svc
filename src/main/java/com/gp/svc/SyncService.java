package com.gp.svc;

import com.gp.common.ServiceContext;
import com.gp.dao.info.SyncMsgOutInfo;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;

public interface SyncService {

	Boolean newSyncMsgOut(ServiceContext svcctx, SyncMsgOutInfo syncMsgOut) throws ServiceException;
}
