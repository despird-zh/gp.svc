package com.gp.dao;

import java.util.List;

import com.gp.info.CabVersionInfo;
import com.gp.info.InfoId;

public interface CabVersionDAO extends BaseDAO<CabVersionInfo>{

	public List<CabVersionInfo> queryByFileId(Long fileid);
}
