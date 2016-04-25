package com.gp.dao;

import java.util.List;

import com.gp.info.CabVersionInfo;

public interface CabVersionDAO extends BaseDAO<CabVersionInfo>{

	public List<CabVersionInfo> queryByFileId(Long fileid);
}
