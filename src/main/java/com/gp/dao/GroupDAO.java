package com.gp.dao;

import com.gp.info.GroupInfo;
import com.gp.info.InfoId;

public interface GroupDAO extends BaseDAO<GroupInfo>{

	public int deleteByName(InfoId<Long> workgroupId, String group);
	
	public GroupInfo queryByName(InfoId<Long> workgroupId, String group);

}
