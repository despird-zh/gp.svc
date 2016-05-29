package com.gp.dao;

import com.gp.info.IdSettingInfo;
import com.gp.info.Identifier;

public interface IdSettingDAO{
	
	IdSettingInfo queryByIdKey( Identifier idKey);
	
	int updateByIdKey(String modifier, Identifier idKey, Long nextValue);
}
