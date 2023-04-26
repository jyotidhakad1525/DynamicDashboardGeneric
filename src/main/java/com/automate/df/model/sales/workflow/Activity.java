package com.automate.df.model.sales.workflow;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class Activity extends com.automate.df.model.sales.workflow.WorkflowContext {

    private int activityId;
    private Date activityActualEndTime;
    private Date activityActualStartTime;
    private Date activityCreatedTime;
    private Date activityExpectedEndTime;
    private Date activityExpectedStartTime;
    private String activityName;
    private int activitySequence;
    private String activityStatus;
    private Date activityUpdateTime;
    private String approverId;
    private String assigneeId;
    private String errorDetail;
    private String executionJob;
    private Boolean isError;
    private Boolean isLastTask;
    private Boolean isMandatoryActivity;
    private String entityStatus;
    private int nextActivityId;
    private Boolean repeatActivity;
    private Boolean successState;
    private String triggerType;
    private String universalId;

    private Integer entityId;

    private Process DMSProcess;
    private List<Task> activitytasks;


    public Boolean getError() {
        return isError;
    }

    public void setError(Boolean error) {
        isError = error;
    }

    public Boolean getLastTask() {
        return isLastTask;
    }

    public void setLastTask(Boolean lastTask) {
        isLastTask = lastTask;
    }


}
