package com.automate.df.model.sales.workflow;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class Process extends com.automate.df.model.sales.workflow.WorkflowContext {

    private int processId;
    private String approverId;
    private String assigneeId;
    private String errorDetail;
    private Boolean isError;
    private Boolean isLastTask;
    private Boolean isMandatoryProcess;
    private String entityStatus;
    private Integer nextProcessId;
    private Date processActualEndTime;
    private Date processActualStartTime;
    private Date processCreatedTime;
    private Date processExpectedEndTime;
    private Date processExpectedStartTime;
    private String processName;
    private Integer processSequence;
    private String processStatus;
    private Date processUpdatedTime;
    private Boolean repeatProcess;
    private Boolean successState;
    private String universalId;
    private List<com.automate.df.model.sales.workflow.Task> tasks;
    private Integer entityId;

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
