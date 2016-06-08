package com.gp.dao;

import java.util.Map;

import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.info.RolePageInfo;

public interface RolePageDAO extends BaseDAO<RolePageInfo>{

	public int update(InfoId<Integer> roleId, InfoId<Integer> pageId, Map<FlatColLocator, Boolean> perms);
}
