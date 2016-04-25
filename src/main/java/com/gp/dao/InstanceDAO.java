package com.gp.dao;

import com.gp.info.InfoId;
import com.gp.info.InstanceInfo;

public interface InstanceDAO extends BaseDAO<InstanceInfo>{

	public InstanceInfo queryByHashKey(String hashKey);
	
	public int updateState(InfoId<Integer> instanceId, String state);
	
	public InstanceInfo queryByCodes(String entity, String node);
}
