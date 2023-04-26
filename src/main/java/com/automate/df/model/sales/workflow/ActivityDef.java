package com.automate.df.model.sales.workflow;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class ActivityDef {

    private int activityDefId;
    private Date activityCreatedTime;
    private Date activityEndTime;
    private Date activityExpectedEndTime;
    private String activityName;
    private Integer activitySequence;
    private Date activityUpdateTime;
    private String executionJob;
    private Boolean isLastActivity;
    private Boolean isMandatoryActivity;
    private Integer nextActivityId;
    private Integer processDefId;
    private Boolean repeatProcess;
    private String triggerType;
    private List<com.automate.df.model.sales.workflow.Activity> workflowActivities;

    public void setMandatoryActivity(Boolean mandatoryActivity) {
        isMandatoryActivity = mandatoryActivity;
    }

}
