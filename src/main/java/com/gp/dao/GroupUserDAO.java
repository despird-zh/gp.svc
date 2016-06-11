package com.gp.dao;

import com.gp.info.GroupUserInfo;
import com.gp.info.InfoId;

public interface GroupUserDAO extends BaseDAO<GroupUserInfo>{

	public int deleteByAccount(InfoId<Long> membergroupId, String account);
	
	public InfoId<Long> existByAccount(InfoId<Long> membergroupId, String account);
	
	public int deleteByGroup(InfoId<Long> membergroupId);
	
	public int deleteMemberByAccount(InfoId<Long> wgroupId, String account);
}
