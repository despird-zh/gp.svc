package com.gp.dao;

import java.util.List;

import com.gp.info.RoleInfo;

public interface RoleDAO extends BaseDAO<RoleInfo>{
	
	List<RoleInfo> queryAll();
}
