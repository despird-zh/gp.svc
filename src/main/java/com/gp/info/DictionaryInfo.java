package com.gp.info;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

public class DictionaryInfo extends TraceableInfo<Long>{
	
	private static final long serialVersionUID = 1L;

	@NotNull
	private String group;
	
	@NotNull
	private String key;
	
	@NotNull
	private String value;

	@NotNull
	private String defaultLang;

	private Map<FlatColLocator, String> labelMap = new HashMap<FlatColLocator, String>();
	
	public String getDefaultLang() {
		return defaultLang;
	}

	public void setDefaultLang(String defaultLang) {
		this.defaultLang = defaultLang;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
		
	public Map<FlatColLocator, String> getLabelMap() {
		return labelMap;
	}

	public void setLabelMap(Map<FlatColLocator, String> labelMap) {
		this.labelMap = labelMap;
	}
	
	public String getLabel(FlatColLocator col){
		return labelMap.get(col);
	}
	
	public void putLabel(FlatColLocator col, String label){
		labelMap.put(col, value);
	}
}
