package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Task {
	
	public int taskId;
    public String customerRemarks="";
    public String employeeRemarks="";
    public String errorDetail="";
    public String executionJob="";
    public String isError="";
    public boolean isLastTask;
    public boolean isMandatoryTask;
    public String entityStatus="";
    public String reason="";
    public String repeatTask="";
    public String taskActualEndTime="";
    public String taskActualStartTime="";
    public String taskCreatedTime="";
    public String taskExpectedEndTime="";
    public String taskExpectedStartTime="";
    public String taskName="";
    public int taskSequence;
    public String taskStatus="";
    public String taskType="";
    public String taskUpdatedTime="";
    public String triggerType="";
    public String universalId="";
    public boolean isParallelTask;
    public Assignee assignee;
    public String approver="";
    public Api api;
    public SubmitApi submitApi;
    public int entityId;
    public int processId;
    public TaskCategory taskCategory;
    public String entityModuleId="";
    public String entityName="";
    public int processDefintionId;
    public int taskDefinitionId;
    public String leadDto="";
    public String taskStageStatus="";
    public int evaluationApproverId;
    public String taskStatusList="";
    public boolean lastTask;
    public boolean mandatoryTask;
    public boolean parallelTask;
    public String error="";
    
    
    

}
