package com.gp.svc;

import java.util.List;

import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.info.BinaryInfo;
import com.gp.info.InfoId;
import com.gp.info.StorageInfo;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;

public interface StorageService {

	public boolean newBinary(ServiceContext<?> svcctx, BinaryInfo binary) throws ServiceException;
	
	public BinaryInfo getBinaryByHash(ServiceContext<?> svcctx, String hashstr)throws ServiceException;
	
	public BinaryInfo getBinary(ServiceContext<?> svcctx, InfoId<Long> id) throws ServiceException;
	
	public boolean removeBinary(ServiceContext<?> svcctx, InfoId<Long> id) throws ServiceException;
	
	public boolean newStorage(ServiceContext<?> svcctx, StorageInfo storage)throws ServiceException;

	public StorageInfo getStorage(ServiceContext<?> svcctx, InfoId<Integer> id)throws ServiceException;
	
	public boolean existStorage(ServiceContext<?> svcctx, String storagename)throws ServiceException;
	
	public void updateStorage(ServiceContext<?> svcctx, InfoId<Integer> id, int used)throws ServiceException;
	
	public void updateStorage(ServiceContext<?> svcctx, StorageInfo storage)throws ServiceException;
	
	public boolean removeStorage(ServiceContext<?> svcctx, InfoId<Integer> id)throws ServiceException;
	
	public List<StorageInfo> getStorages(ServiceContext<?> svcctx, String storagename, String[] types, String[] states) throws ServiceException;
	
	public PageWrapper<StorageInfo> getStorages(ServiceContext<?> svcctx, String storagename, PageQuery pagequery) throws ServiceException;
	
}
