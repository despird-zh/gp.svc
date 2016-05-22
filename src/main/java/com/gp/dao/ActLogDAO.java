package com.gp.dao;

import java.util.List;

import com.gp.info.ActLogInfo;
import com.gp.info.InfoId;

public interface ActLogDAO extends BaseDAO<ActLogInfo>{

	public List<ActLogInfo> queryByAccount(String account);
	
	public List<ActLogInfo> queryByWorkgroup(InfoId<Long> wid);
	
	public List<ActLogInfo> queryByObject(InfoId<Long> objectid);
}
