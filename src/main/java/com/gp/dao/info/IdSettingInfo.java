package com.gp.dao.info;

import com.gp.info.TraceableInfo;

public class IdSettingInfo extends TraceableInfo<String> {
	
	private static final long serialVersionUID = 1L;

	String idKey;
	
	String idName;
	
	Long currValue;
	
	int stepIncrement;
	
	int length;
	
	String prefix;
	
	String padChar;

	public String getIdKey() {
		return idKey;
	}

	public void setIdKey(String idKey) {
		this.idKey = idKey;
	}

	public String getIdName() {
		return idName;
	}

	public void setIdName(String idName) {
		this.idName = idName;
	}

	public Long getCurrValue() {
		return currValue;
	}

	public void setCurrValue(Long currValue) {
		this.currValue = currValue;
	}

	public int getStepIncrement() {
		return stepIncrement;
	}

	public void setStepIncrement(int stepIncrement) {
		this.stepIncrement = stepIncrement;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPadChar() {
		return padChar;
	}

	public void setPadChar(String padChar) {
		this.padChar = padChar;
	}		
	
}
