package com.gp.dao;

import com.gp.info.InfoId;
import com.gp.info.WorkgroupUserInfo;

public interface WorkgroupUserDAO extends BaseDAO<WorkgroupUserInfo>{
	
	public WorkgroupUserInfo queryByAccount(Long workgroupId, String account);
}
