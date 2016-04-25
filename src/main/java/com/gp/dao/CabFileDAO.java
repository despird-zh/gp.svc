package com.gp.dao;

import java.util.List;

import com.gp.info.CabFileInfo;

public interface CabFileDAO extends BaseDAO<CabFileInfo>{
	
	public List<CabFileInfo> queryByParent(Long parentFolderId);
}
