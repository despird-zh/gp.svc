package com.gp.info;

/**
 * this class wrap the work group summary data
 * 
 * @author garydiao
 * @version 0.1 2015-5-4
 * 
 **/
public class WorkgroupSumInfo extends TraceableInfo<Long>{
	
	private static final long serialVersionUID = -5266848721368318707L;

	private Long workgroupId;
	
	private Integer fileSummary;
	
	private Integer taskSummary;
	
	private Integer memberSummary;
	
	private Integer publishSummary;
	
	private Integer postSummary;
	
	private Integer netdiskSummary;

	public Long getWorkgroupId() {
		return workgroupId;
	}

	public void setWorkgroupId(Long workgroupId) {
		this.workgroupId = workgroupId;
	}

	public Integer getFileSummary() {
		return fileSummary;
	}

	public void setFileSummary(Integer fileSummary) {
		this.fileSummary = fileSummary;
	}

	public Integer getTaskSummary() {
		return taskSummary;
	}

	public void setTaskSummary(Integer taskSummary) {
		this.taskSummary = taskSummary;
	}

	public Integer getMemberSummary() {
		return memberSummary;
	}

	public void setMemberSummary(Integer memberSummary) {
		this.memberSummary = memberSummary;
	}

	public Integer getPublishSummary() {
		return publishSummary;
	}

	public void setPublishSummary(Integer publishSummary) {
		this.publishSummary = publishSummary;
	}

	public Integer getPostSummary() {
		return postSummary;
	}

	public void setPostSummary(Integer postSummary) {
		this.postSummary = postSummary;
	}

	public Integer getNetdiskSummary() {
		return netdiskSummary;
	}

	public void setNetdiskSummary(Integer netdiskSummary) {
		this.netdiskSummary = netdiskSummary;
	}

}
