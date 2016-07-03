package com.gp.dao;

import java.util.List;

import com.gp.info.InfoId;
import com.gp.info.OrgHierInfo;

public interface OrgHierDAO extends BaseDAO<OrgHierInfo>{

	List<OrgHierInfo> queryByIds(InfoId<?> ... ids);
}
