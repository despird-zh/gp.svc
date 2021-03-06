package com.gp.svc;

import java.util.List;

import com.gp.acl.Ace;
import com.gp.acl.Acl;
import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.dao.info.CabFileInfo;
import com.gp.dao.info.CabVersionInfo;
import com.gp.dao.info.CabinetInfo;
import com.gp.info.InfoId;
import com.gp.dao.info.StorageInfo;

public interface FileService {

	/**
	 * get versions of a file
	 * @param filekey the key of file 
	 **/
	public List<CabVersionInfo> getVersions(ServiceContext svcctx, InfoId<Long> filekey) throws ServiceException;
	
	/**
	 * create new file 
	 * @param file the cabinet file information
	 **/
	public InfoId<Long> newFile(ServiceContext svcctx, CabFileInfo file, Acl acl) throws ServiceException;
	
	/**
	 * copy the file to target path 
	 * @param srcfilekey the file key of source file 
	 * @param destinationPkey the destination parent key.
	 **/
	public InfoId<Long> copyFile(ServiceContext svcctx, InfoId<Long> srcfilekey, InfoId<Long> destinationPkey) throws ServiceException;

	/**
	 * move the file to target path
	 * @param srcfilekey the file key of source file 
	 * @param destinationPkey the destination parent key.
	 **/
	public boolean moveFile(ServiceContext svcctx, InfoId<Long> srcfileid, InfoId<Long> destinationId) throws ServiceException;
	
	/**
	 * create new version of file
	 * @param filekey the key of cabinet file
	 **/
	public InfoId<Long> newVersion(ServiceContext svcctx, InfoId<Long> filekey) throws ServiceException;

	/**
	 * add ace information 
	 * @param srcfilekey the key of source file
	 * @param ace the ace item of source file
	 **/
	public void addAce(ServiceContext svcctx, InfoId<Long> srcfilekey, Ace ace )throws ServiceException;
	
	/**
	 * remove ace information 
	 * @param srcfilekey the key of source file
	 * @param ace the ace item of source file
	 **/
	public void removeAce(ServiceContext svcctx, InfoId<Long> srcfilekey, String type, String subject )throws ServiceException;
	
	/**
	 * add acl information 
	 * @param srcfilekey the key of source file
	 * @param acl the acl of source file
	 **/
	public void addAcl(ServiceContext svcctx, InfoId<Long> srcfilekey, Acl acl )throws ServiceException;
	
	/**
	 * get the storage information of a cabinet file 
	 **/
	public StorageInfo getStorage(InfoId<Long> fileid) throws ServiceException;
	
	/**
	 * find cabinet information by file id 
	 * @param fileid the id of file which reside in target cabinet
	 **/
	public CabinetInfo getCabinetInfo(ServiceContext svcctx, InfoId<Long> fileid)throws ServiceException;
	
	/**
	 * find the file information 
	 * @param fileid the id of file 
	 **/
	public CabFileInfo getFile(ServiceContext svcctx, InfoId<Long> fileid) throws ServiceException;
}
