package com.gp.dao;

import java.util.List;

import com.gp.info.InfoId;
import com.gp.info.WorkgroupInfo;

public interface WorkgroupDAO extends BaseDAO<WorkgroupInfo>{

	List<WorkgroupInfo> queryByIds(InfoId<?> ... ids);
}
