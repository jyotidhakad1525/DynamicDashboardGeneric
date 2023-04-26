package com.automate.df.model.sales.workflow;

import com.automate.df.entity.sales.workflow.DMSTask;
import com.automate.df.model.sales.lead.EmployeeDTO;
import com.automate.df.model.sales.lead.LeadDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class Task extends com.automate.df.model.sales.workflow.WorkflowContext {

    private int taskId;
    private String customerRemarks;
    private String employeeRemarks;
    private String errorDetail;
    private String executionJob;
    private Boolean isError;
    private Boolean isLastTask;
    private Boolean isMandatoryTask;
    private String entityStatus;
    private String reason;
    private String repeatTask;
    private Date taskActualEndTime;
    private Date taskActualStartTime;
    private Date taskCreatedTime;
    private Date taskExpectedEndTime;
    private Date taskExpectedStartTime;
    private String taskName;
    private Integer taskSequence;
    private String taskStatus;
    private String taskType;
    private Date taskUpdatedTime;
    private String triggerType;
    private String universalId;
    private Boolean isParallelTask;
    private EmployeeDTO assignee;
    private EmployeeDTO approver;
    private Api api;
    private Api submitApi;
    private Integer entityId;
    private Integer processId;
    private com.automate.df.model.sales.workflow.TaskCategory taskCategory;
    private String entityModuleId;
    private String entityName;
    private int processDefintionId;
    private int taskDefinitionId;
    private LeadDto leadDto;
    private String taskStageStatus;
    private int evaluationApproverId;
    private List<String> taskStatusList;

    public Task(DMSTask task, LeadDto leadDto, com.automate.df.model.sales.workflow.TaskCategory taskcategory, EmployeeDTO assignee, EmployeeDTO approver) {
        this.taskId = task.getTaskId();
        this.customerRemarks = task.getCustomerRemarks();
        this.employeeRemarks = task.getEmployeeRemarks();
        this.errorDetail = task.getErrorDetail();
        this.executionJob = task.getExecutionJob();
        this.isError = task.getIsError();
        this.isLastTask = task.getIsLastTask();
        this.isMandatoryTask = task.getIsMandatoryTask();
        this.entityStatus = task.getEntityStatus();
        this.reason = task.getReason();
        this.repeatTask = task.getRepeatTask();
        this.taskActualEndTime = task.getTaskActualEndTime();
        this.taskActualStartTime = task.getTaskActualStartTime();
        this.taskCreatedTime = task.getTaskCreatedTime();
        this.taskExpectedEndTime = task.getTaskExpectedEndTime();
        this.taskExpectedStartTime = task.getTaskExpectedStartTime();
        this.taskName = task.getTaskName();
        this.taskSequence = task.getTaskSequence();
        this.taskStatus = task.getTaskStatus();
        this.taskType = task.getTaskType();
        this.taskUpdatedTime = task.getTaskUpdatedTime();
        this.triggerType = task.getTriggerType();
        this.universalId = task.getUniversalId();
        this.isParallelTask = task.getParallelTask();
        this.assignee = assignee;
        this.approver = approver;
        this.entityId = task.getEntityId();
        this.processId = task.getDmsProcess().getProcessId();
        this.taskCategory = taskcategory;
        this.entityModuleId = task.getEntityModuleId();
        this.entityName = task.getEntityName();
        this.leadDto = leadDto;
        this.taskStageStatus = task.getTaskStageStatus();
        this.approver = approver;
    }

    public Boolean getParallelTask() {
        return isParallelTask;
    }

    public void setParallelTask(Boolean parallelTask) {
        isParallelTask = parallelTask;
    }

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

    public Boolean getMandatoryTask() {
        return isMandatoryTask;
    }

    public void setMandatoryTask(Boolean mandatoryTask) {
        isMandatoryTask = mandatoryTask;
    }

}
