package com.gp.info;

public class TagRelInfo extends TraceableInfo<Long>{
	
	private static final long serialVersionUID = 1L;

	private Long resourceId;
	
	private String tagType;
	
	private String tagName;
	
	private String category;

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		this.tagType = tagType;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	
}
