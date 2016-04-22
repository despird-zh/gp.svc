package com.gp.info;

public class GroupInfo extends TraceableInfo<Long>{

	private static final long serialVersionUID = 1L;

	private Long workgroupId;
	
	private String groupName;
	
	private String description;

	public Long getWorkgroupId() {
		return workgroupId;
	}

	public void setWorkgroupId(Long workgroupId) {
		this.workgroupId = workgroupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
