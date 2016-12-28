package com.gp.dao.info;

import com.gp.info.TraceableInfo;

import java.util.Date;

/**
 * Created by garydiao on 10/4/16.
 */
public class ProcTrailInfo extends TraceableInfo<Long>{

    private Long procId;

    private Long stepId;

    private String opinion;

    private String comment;

    private Date executeTime;

    private String executor;

    public Long getProcId() {
        return procId;
    }

    public void setProcId(Long procId) {
        this.procId = procId;
    }

    public Long getStepId() {
        return stepId;
    }

    public void setStepId(Long stepId) {
        this.stepId = stepId;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }
}
