package com.gp.dao;

import com.gp.info.GroupUserInfo;
import com.gp.info.InfoId;

public interface GroupUserDAO extends BaseDAO<GroupUserInfo>{

	public int deleteByAccount(InfoId<Long> groupId, String account);
	
	public boolean existByAccount(InfoId<Long> groupId, String account);
	
	public int deleteByGroup(InfoId<Long> groupId);
}
