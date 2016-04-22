package com.gp.dao;

import org.springframework.jdbc.core.RowMapper;

import com.gp.info.InfoId;
import com.gp.info.WorkgroupExInfo;
import com.gp.info.WorkgroupInfo;

public interface WorkgroupDAO extends BaseDAO<WorkgroupInfo>{

	public RowMapper<WorkgroupExInfo> getWorkgroupExRowMapper();
}
