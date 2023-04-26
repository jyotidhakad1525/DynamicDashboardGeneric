package com.automate.df.model.sales.workflow;

import com.automate.df.model.sales.master.Branch;
import com.automate.df.model.sales.master.Organization;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class WorkflowDef {

    private int workflowDefId;
    private String executionJob;
    private Boolean isLastTask;
    private Boolean repeatProcess;
    private String triggerType;
    private Date workflowCreatedTime;
    private Date workflowEndTime;
    private Date workflowExpectedEndTime;
    private String workflowName;
    private Integer workflowSequence;
    private Date workflowUpdatedTime;
    private List<com.automate.df.model.sales.workflow.Workflow> dmsWorkflows;
    private Organization dmsOrganization;
    private Branch dmsBranch;

    public Boolean getLastTask() {
        return isLastTask;
    }

    public void setLastTask(Boolean lastTask) {
        isLastTask = lastTask;
    }


}
