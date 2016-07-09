package com.gp.info;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GroupInfo extends TraceableInfo<Long>{

	private static final long serialVersionUID = 1L;

	@NotNull
	@Min(value=1)
	private Long manageId;
	
	@NotNull
	@Size(min=1, max=16)
	private String groupName;
	
	@NotNull
	@Size(min=1, max=32)
	private String groupType;
	
	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	private String description;

	public Long getManageId() {
		return manageId;
	}

	public void setManageId(Long manageId) {
		this.manageId = manageId;
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
