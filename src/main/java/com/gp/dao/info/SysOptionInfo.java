package com.gp.dao.info;

import com.gp.info.TraceableInfo;

public class SysOptionInfo extends TraceableInfo<Integer> {

	private static final long serialVersionUID = 1L;

	String optionGroup;
	
	String optionKey;
	
	String optionValue;

	String description;

	public String getOptionGroup() {
		return optionGroup;
	}

	public void setOptionGroup(String optionGroup) {
		this.optionGroup = optionGroup;
	}

	public String getOptionKey() {
		return optionKey;
	}

	public void setOptionKey(String optionKey) {
		this.optionKey = optionKey;
	}

	public String getOptionValue() {
		return optionValue;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
