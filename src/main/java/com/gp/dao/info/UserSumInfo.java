package com.gp.dao.info;

import com.gp.info.TraceableInfo;

/**
 * this class wrap the summary data of user
 * 
 * @author garydiao
 * @version 0.1 2015-5-4
 * 
 **/
public class UserSumInfo extends TraceableInfo<Long> {

	private static final long serialVersionUID = 5728500858516252991L;

	private String account;

	private Integer fileSummary;
	
	private Integer taskSummary;
	
	private Integer shareSummary;
	
	private Integer postSummary;
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
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

	public Integer getShareSummary() {
		return shareSummary;
	}

	public void setShareSummary(Integer shareSummary) {
		this.shareSummary = shareSummary;
	}

	public Integer getPostSummary() {
		return postSummary;
	}

	public void setPostSummary(Integer postSummary) {
		this.postSummary = postSummary;
	}
	
	
}
