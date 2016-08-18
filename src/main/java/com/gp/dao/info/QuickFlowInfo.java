package com.gp.dao.info;

import com.gp.info.TraceableInfo;

/**
 * Created by garydiao on 8/18/16.
 */
public class QuickFlowInfo extends TraceableInfo<Long>{

    private String category;

    private String flowName;

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
}
