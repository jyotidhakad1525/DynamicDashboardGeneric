package com.automate.df.model.sales.workflow;

import com.automate.df.model.sales.master.Branch;
import com.automate.df.model.sales.master.Organization;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ProcessDef {

    private int processDefId;
    private String executionJob;
    private Boolean isLastTask;
    private Boolean isMandatoryProcess;
    private Integer nextProcessId;
    private Date processCreatedTime;
    private Date processEndTime;
    private Date processExpectedEndTime;
    private String processName;
    private Integer processSequence;
    private Date processUpdatedTime;
    private Boolean repeatProcess;
    private String triggerType;
    private Integer workflowDefId;
    private Organization dmsOrganization;
    private Branch dmsBranch;

    public Boolean getLastTask() {
        return isLastTask;
    }

    public void setLastTask(Boolean lastTask) {
        isLastTask = lastTask;
    }

    public void setMandatoryProcess(Boolean mandatoryProcess) {
        isMandatoryProcess = mandatoryProcess;
    }


}
