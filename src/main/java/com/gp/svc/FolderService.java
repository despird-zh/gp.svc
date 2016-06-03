package com.gp.svc;

import com.gp.acl.Ace;
import com.gp.acl.Acl;
import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.info.CabFolderInfo;
import com.gp.info.CabinetInfo;
import com.gp.info.InfoId;

public interface FolderService {

	/**
	 * create folder 
	 **/
	public InfoId<Long> newFolder(ServiceContext<?> svcctx, InfoId<Long> parentkey, CabFolderInfo file, Acl acl) throws ServiceException;

	/**
	 * copy the folder to target path
	 * @param svcctx the context of service
	 * @param folderkey the key of source folder object
	 * @param distinationPkey the parent folder key of destination
	 **/
	public InfoId<Long> copyFolder(ServiceContext<?> svcctx, InfoId<Long> folderkey, InfoId<Long> destinationPkey) throws ServiceException;

	/**
	 * move the folder to target path
	 **/
	public boolean moveFolder(ServiceContext<?> svcctx, InfoId<Long> folderkey, InfoId<Long> destinationPkey) throws ServiceException;
	
	/**
	 * add ace information
	 **/
	public void addAce(ServiceContext<?> svcctx, InfoId<Long> folderkey, Ace ... ace )throws ServiceException;
	
	/**
	 * add acl information 
	 **/
	public void addAcl(ServiceContext<?> svcctx, InfoId<Long> folderkey,  Acl acl )throws ServiceException;

	/**
	 * find cabinet information by folder id 
	 * @param folderid the id of folder which reside in target cabinet
	 **/
	public CabinetInfo getCabinetInfo(ServiceContext<?> svcctx, InfoId<Long> folderid)throws ServiceException;
	
	/**
	 * Find the folder information 
	 * @param folderid the id of folder
	 **/
	public CabFolderInfo getFolder(ServiceContext<?> svcctx, InfoId<Long> folderid) throws ServiceException;
	
	public InfoId<Long> getFolderId(ServiceContext<?> svcctx, InfoId<Long> cabientId, String path) throws ServiceException;
	
	public String getFolderPath(ServiceContext<?> svcctx, InfoId<Long> folderId) throws ServiceException;
}
