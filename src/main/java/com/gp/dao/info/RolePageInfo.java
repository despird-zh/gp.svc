package com.gp.dao.info;

import com.gp.info.FlatColLocator;
import com.gp.info.TraceableInfo;

import java.util.HashMap;
import java.util.Map;

public class RolePageInfo extends TraceableInfo<Integer> {

	private static final long serialVersionUID = 6963210591819570225L;

	private Integer roleId;
	
	private Integer pageId;
	
	private Map<FlatColLocator, Boolean> permMap = new HashMap<FlatColLocator, Boolean>();

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getPageId() {
		return pageId;
	}

	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}

	public Map<FlatColLocator, Boolean> getPermMap() {
		return permMap;
	}

	public void setPermMap(Map<FlatColLocator, Boolean> permMap) {
		this.permMap = permMap;
	}

	public Boolean getColValue(FlatColLocator col){
		return permMap.get(col);
	}
	
	public void putColValue(FlatColLocator col, Boolean value){
		permMap.put(col, value);
	}
}
