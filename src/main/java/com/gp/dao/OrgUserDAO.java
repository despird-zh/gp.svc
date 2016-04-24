package com.gp.dao;

import com.gp.info.InfoId;
import com.gp.info.OrgUserInfo;

public interface OrgUserDAO extends BaseDAO<OrgUserInfo>{

	/**
	 * delete user under organization node by account
	 **/
	public int deleteByAccount(InfoId<Long> orgId, String account);
	
	
	public int deleteByOrgHier(InfoId<Long> orgId);
}
