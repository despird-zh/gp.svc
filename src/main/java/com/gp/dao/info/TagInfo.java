package com.gp.dao.info;

import com.gp.info.TraceableInfo;

public class TagInfo extends TraceableInfo<Long> {
	
	private static final long serialVersionUID = 1L;

	private String tagName;
	
	private String tagType;
	
	private String tagColor;
	
	private String category;

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		this.tagType = tagType;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTagColor() {
		return tagColor;
	}

	public void setTagColor(String tagColor) {
		this.tagColor = tagColor;
	}

	
}
