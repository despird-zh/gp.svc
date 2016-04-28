package com.gp.info;

import java.util.HashMap;
import java.util.Map;

public class PageInfo extends TraceableInfo<Integer>{

	private static final long serialVersionUID = -6041975727676516623L;

	private String pageName;
	
	private String module;
	
	private String description;
	
	private String pageAbbr;
	
	private Map<FlatColLocator, String> actionMap = new HashMap<FlatColLocator, String>();

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPageAbbr() {
		return pageAbbr;
	}

	public void setPageAbbr(String pageAbbr) {
		this.pageAbbr = pageAbbr;
	}

	public Map<FlatColLocator, String> getActionMap() {
		return actionMap;
	}

	public void setActionMap(Map<FlatColLocator, String> actionMap) {
		this.actionMap = actionMap;
	}
	
	public String getColValue(FlatColLocator col){
		return actionMap.get(col);
	}
	
	public void putColValue(FlatColLocator col, String value){
		actionMap.put(col, value);
	}
}
