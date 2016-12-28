package com.gp.dao.info;

import com.gp.info.TraceableInfo;

/**
 * Created by garydiao on 8/18/16.
 */
public class QuickFlowInfo extends TraceableInfo<Long>{

	private static final long serialVersionUID = 1L;

	private String category;

    private String flowName;

    private int duration;

    private String customProcess;

    public String getCustomProcess() {
        return customProcess;
    }

    public void setCustomProcess(String customProcess) {
        this.customProcess = customProcess;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
}
