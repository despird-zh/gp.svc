package com.gp.dao;

import java.util.List;

import com.gp.info.CabFolderInfo;
import com.gp.info.InfoId;

public interface CabFolderDAO extends BaseDAO<CabFolderInfo>{

	/**
	 * Query the folder information by folder name
	 * @param foldername the name of folder
	 * @param parentKey the key of parent folder. 
	 **/
	public CabFolderInfo queryByName(InfoId<Long> parentKey, String foldername);

	public List<CabFolderInfo> queryByParent(Long parentFolderId);
}
