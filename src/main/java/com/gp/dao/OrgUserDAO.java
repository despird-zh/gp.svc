package com.gp.dao;

import com.gp.info.InfoId;
import com.gp.info.OrgUserInfo;

public interface OrgUserDAO extends BaseDAO<OrgUserInfo>{

	public int deleteByAccount(Long orgId, String account);
}
