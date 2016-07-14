package com.gp.dao.info;

import com.gp.info.FlatColLocator;
import com.gp.info.TraceableInfo;

import java.util.HashMap;
import java.util.Map;

public class UserRoleInfo extends TraceableInfo<Long> {

	private static final long serialVersionUID = 6346256008561149468L;
	
	private Long userId;
	
	private Map<FlatColLocator, Integer> roleMap = new HashMap<FlatColLocator, Integer>();

	public Map<FlatColLocator, Integer> getRoleMap() {
		return roleMap;
	}

	public void setRoleMap(Map<FlatColLocator, Integer> roleMap) {
		this.roleMap = roleMap;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getColValue(FlatColLocator col){
		return roleMap.get(col);
	}
	
	public void putColValue(FlatColLocator col, Integer value){
		roleMap.put(col, value);
	}
}
