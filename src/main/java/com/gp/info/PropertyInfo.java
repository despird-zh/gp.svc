package com.gp.info;

public class PropertyInfo extends TraceableInfo<Long>{

	private static final long serialVersionUID = 1L;

	private String label;
	
	private String type;
	
	private String defaultValue;
	
	private String enumValues;
	
	private String format;

	public String getLabel() {
		return label;
	}

	public void setLabel(String labelJson) {
		this.label = labelJson;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getEnumValues() {
		return enumValues;
	}

	public void setEnumValues(String enumValues) {
		this.enumValues = enumValues;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

}
