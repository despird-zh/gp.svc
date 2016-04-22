package com.gp.svc;

import java.util.List;

import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.info.CabEntryInfo;
import com.gp.info.CabFileInfo;
import com.gp.info.CabFolderInfo;
import com.gp.info.CabinetInfo;
import com.gp.info.InfoId;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;

/**
 * Cabinet service provides the methods to browse the content of cabinets 
 **/
public interface CabinetService {

	/**
	 * Get the personal cabinets of specified account 
	 **/
	public List<CabinetInfo> getCabinets(ServiceContext<?> svcctx, String account)throws ServiceException;

	/**
	 * Get cabinet infomation 
	 **/
	public CabinetInfo getCabinet(ServiceContext<?> svcctx, InfoId<Long> ckey) throws ServiceException;

	/**
	 * Get cabinet folders, with folder name filter
	 **/
	public List<CabFolderInfo> getCabFolders(ServiceContext<?> svcctx, 
			InfoId<Long> ckey,
			InfoId<Long> folderkey,
			String foldername) throws ServiceException;

	/**
	 * Get cabinet folders, with folder name filter
	 **/
	public PageWrapper<CabFolderInfo> getCabFolders(ServiceContext<?> svcctx, 
			InfoId<Long> ckey,
			InfoId<Long> folderkey,
			String foldername, 
			PageQuery pquery) throws ServiceException;
	
	/**
	 * Get cabinet folders under parent folder path 
	 **/
	public List<CabFolderInfo> getCabFolders(ServiceContext<?> svcctx, 
			InfoId<Long> ckey,
			InfoId<Long> folderkey) throws ServiceException;

	/**
	 * Get cabinet files under specified folder path
	 **/
	public List<CabFileInfo> getCabFiles(ServiceContext<?> svcctx, 
			InfoId<Long> ckey,
			InfoId<Long> folderkey) throws ServiceException;
	
	/**
	 * Get cabinet folders, with folder name filter
	 **/
	public PageWrapper<CabFileInfo> getCabFiles(ServiceContext<?> svcctx, 
			InfoId<Long> ckey,
			InfoId<Long> folderkey,
			String filename, 
			PageQuery pquery) throws ServiceException;
	
	/**
	 * Get cabinet folders, with folder name filter
	 **/
	public PageWrapper<CabEntryInfo> getCabEntries(ServiceContext<?> svcctx, 
			InfoId<Long> ckey,
			InfoId<Long> folderkey,
			String entryname, 
			PageQuery pquery) throws ServiceException;
	
	/**
	 * Get cabinet files, with folder name filter
	 **/
	public List<CabFileInfo> getCabFiles(ServiceContext<?> svcctx, 
			InfoId<Long> ckey,
			InfoId<Long> folderkey,
			String filename) throws ServiceException;
	
}
