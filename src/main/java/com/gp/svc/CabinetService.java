package com.gp.svc;

import java.util.List;

import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.dao.info.CabEntryInfo;
import com.gp.dao.info.CabFileInfo;
import com.gp.dao.info.CabFolderInfo;
import com.gp.dao.info.CabinetInfo;
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
	public List<CabinetInfo> getCabinets(ServiceContext svcctx, String account)throws ServiceException;

	/**
	 * Get cabinet information by cabinet id
	 * 
	 * @param cabid the cabinet id
	 **/
	public CabinetInfo getCabinet(ServiceContext svcctx, InfoId<Long> cabid) throws ServiceException;

	/**
	 * Get cabinet sub folders under a folder in cabinet, with folder name as filter condition
	 * @param cabid the cabinet id
	 * @param folderid the folder id
	 * @param foldername the folder name condition
	 **/
	public List<CabFolderInfo> getCabFolders(ServiceContext svcctx, 
			InfoId<Long> cabid,
			InfoId<Long> folderid,
			String foldername) throws ServiceException;

	/**
	 * Get cabinet sub folders under a folder in cabinet, with folder name as filter condition. 
	 * this method support pagination
	 * 
	 * @param cabid the cabinet id
	 * @param folderid the folder id
	 * @param foldername the folder name condition
	 * @param pquery the pagination setting
	 **/
	public PageWrapper<CabFolderInfo> getCabFolders(ServiceContext svcctx, 
			InfoId<Long> cabid,
			InfoId<Long> folderid,
			String foldername, 
			PageQuery pquery) throws ServiceException;

	/**
	 * Get cabinet files under a folder in cabinet, with file name filter
	 * @param cabid the cabinet id
	 * @param folderid the parent folder id
	 * @param filename the file name filter condition
	 **/
	public List<CabFileInfo> getCabFiles(ServiceContext svcctx, 
			InfoId<Long> cab,
			InfoId<Long> folderid,
			String filename) throws ServiceException;
	
	/**
	 * Get cabinet files under a folder in cabinet, with file name filter, 
	 * this method support pagination
	 * 
	 * @param cabid the cabinet id
	 * @param folderid the parent folder id
	 * @param filename the file name filter condition
	 * @param pquery the pagination setting
	 **/
	public PageWrapper<CabFileInfo> getCabFiles(ServiceContext svcctx, 
			InfoId<Long> cabid,
			InfoId<Long> folderid,
			String filename, 
			PageQuery pquery) throws ServiceException;
	
	/**
	 * Get cabinet entries under a folder in cabinet. with entry name filter
	 * 
	 * @param cabid the cabinet id
	 * @param folderid the parent folder id
	 * @param entryname the entry name filter
	 * @param pquery the pagination setting
	 **/
	public PageWrapper<CabEntryInfo> getCabEntries(ServiceContext svcctx, 
			InfoId<Long> cabid,
			InfoId<Long> folderid,
			String entryname, 
			PageQuery pquery) throws ServiceException;

	
}
