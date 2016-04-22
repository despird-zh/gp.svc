package com.gp.dao;

import com.gp.info.InstanceInfo;
import com.gp.info.InfoId;

public interface InstanceDAO extends BaseDAO<InstanceInfo>{

	public InstanceInfo queryByHashKey(String hashKey);
	
	public int updateState(Integer instanceId, String state);
	
	public InstanceInfo queryByCodes(String entity, String node);
}
