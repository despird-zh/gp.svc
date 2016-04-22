package com.gp.dao;

import java.util.List;

import com.gp.info.InfoId;
import com.gp.info.SysOptionInfo;

public interface SysOptionDAO extends BaseDAO<SysOptionInfo>{

	public List<SysOptionInfo> queryAll() ;
	
	public List<SysOptionInfo> queryByGroup(String groupKey) ;
	
	public SysOptionInfo queryByKey(String optKey) ;
	
	public int updateByKey(String optionKey, String optionValue);
}
