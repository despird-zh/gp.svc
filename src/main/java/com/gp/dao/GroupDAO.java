package com.gp.dao;

import com.gp.info.GroupInfo;
import com.gp.info.InfoId;

public interface GroupDAO extends BaseDAO<GroupInfo>{

	public int deleteByName(Long workgroupId, String group);
	
	public GroupInfo queryByName(Long workgroupId, String group);

}
