package com.gp.svc;

import java.util.List;

import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.info.ShareInfo;
import com.gp.info.ShareItemInfo;

/**
 * 分享功能用于对资源的读取，不涉及更改和写操作 
 **/
public interface ShareService {

	public List<ShareInfo> getShares(ServiceContext svcctx, InfoId<Long> workgroupKey)throws ServiceException;
	
	public InfoId<Long> newShare(ServiceContext svcctx, InfoId<Long> workgroupKey, ShareInfo share)throws ServiceException;
	
	public void removeShare(ServiceContext svcctx, InfoId<Long> workgroupKey, InfoId<Long> shareKey)throws ServiceException;
	
	public ShareInfo getShare(ServiceContext svcctx, InfoId<Long> workgroupKey, InfoId<Long> shareKey)throws ServiceException;
	
	public void updateShare(ServiceContext svcctx, InfoId<Long> workgroupKey, ShareInfo share)throws ServiceException;
	
	public List<ShareItemInfo> getShareItmes(ServiceContext svcctx, InfoId<Long> workgroupKey, InfoId<Long> shareKey)throws ServiceException;
	
	public InfoId<Long> addShareItem(ServiceContext svcctx, 
			InfoId<Long> workgroupKey, 
			InfoId<Long> shareKey,
			ShareItemInfo shareitem)throws ServiceException;
	
	public void removeShareItem(ServiceContext svcctx, 
			InfoId<Long> workgroupKey, 
			InfoId<Long> shareKey, 
			InfoId<Long> shareItemKey)throws ServiceException;

	public ShareItemInfo getShareItem(ServiceContext svcctx, 
			InfoId<Long> workgroupKey, 
			InfoId<Long> shareKey, 
			InfoId<Long> shareItemKey)throws ServiceException;
	
}
