package com.gp.dao;

import com.gp.info.InfoId;
import com.gp.info.SourceInfo;

public interface SourceDAO extends BaseDAO<SourceInfo>{

	public SourceInfo queryByHashKey(String hashKey);
	
	public int updateState(InfoId<Integer> sourceId, String state);
	
	public SourceInfo queryByCodes(String entity, String node);
}
