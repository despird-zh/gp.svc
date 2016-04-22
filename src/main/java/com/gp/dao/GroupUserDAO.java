package com.gp.dao;

import com.gp.info.GroupUserInfo;
import com.gp.info.InfoId;

public interface GroupUserDAO extends BaseDAO<GroupUserInfo>{

	public int deleteByAccount(Long groupId, String account);
	
	public boolean existByAccount(Long groupId, String account);
	
	public int deleteByGroup(Long groupId);
}
