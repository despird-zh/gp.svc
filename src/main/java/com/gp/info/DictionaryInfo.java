package com.gp.info;

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
	private String label;

	@NotNull
	private String defaultLanguage;
	
	private String labelzh_CN;
	
	public String getDefaultLanguage() {
		return defaultLanguage;
	}

	public void setDefaultLanguage(String defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
		
}
