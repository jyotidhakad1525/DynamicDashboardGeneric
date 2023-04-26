package com.automate.df.model.sales.workflow;

import com.automate.df.model.sales.lead.EmployeeDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class Workflow extends com.automate.df.model.sales.workflow.WorkflowContext {

    private int workflowId;
    private Integer currentActivity;
    private Integer currentProcess;
    private String errorDetail;
    private Boolean isError;
    private Boolean isLastTask;
    private Integer lastActivityCompleted;
    private Integer lastProcessCompleted;
    private Integer lastTaskCompleted;
    private String entityStatus;
    private Boolean repeatWorkflow;
    private Boolean successState;
    private String universalId;
    private Date workflowActualEndTime;
    private Date workflowActualStartTime;
    private Integer workflowCreatedBy;
    private Date workflowCreatedTime;
    private Date workflowExpectedEndTime;
    private Date workflowExpectedStartTime;
    private String workflowName;
    private Integer workflowSequence;
    private String workflowStatus;
    private Integer workflowUpdatedBy;
    private Date workflowUpdatedTime;
    private EmployeeDTO assignee;
    private EmployeeDTO approver;
    private List<Process> dmsProcess;

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
