package com.automate.df.model.sales.workflow;

import com.automate.df.entity.sales.workflow.DmsApi;
import com.automate.df.model.sales.master.Branch;
import com.automate.df.model.sales.master.Organization;

import java.util.Date;

public class TaskDef {

    private int taskDefId;
    private Integer activityDefId;
    private String executionJob;
    private boolean isLastTask;
    private Boolean isMandatoryTask;
    private DmsApi loadApi;
    private Integer nextTaskId;
    private Boolean repeatTask;
    private com.automate.df.model.sales.workflow.Api submitApi;
    private Date taskCreatedTime;
    private Date taskEndTime;
    private Date taskExpectedEndTime;
    private String taskName;
    private Integer taskSequence;
    private String taskType;
    private Date taskUpdateTime;
    private String triggerType;
    private com.automate.df.model.sales.workflow.TaskCategory dmsTaskCategory;
    private Organization dmsOrganization;
    private Branch dmsBranch;
    private com.automate.df.model.sales.workflow.Api dmsApi;


    public int getTaskDefId() {
        return taskDefId;
    }

    public void setTaskDefId(int taskDefId) {
        this.taskDefId = taskDefId;
    }

    public Integer getActivityDefId() {
        return activityDefId;
    }

    public void setActivityDefId(Integer activityDefId) {
        this.activityDefId = activityDefId;
    }

    public String getExecutionJob() {
        return executionJob;
    }

    public void setExecutionJob(String executionJob) {
        this.executionJob = executionJob;
    }

    public boolean isLastTask() {
        return isLastTask;
    }

    public void setLastTask(boolean lastTask) {
        isLastTask = lastTask;
    }

    public Boolean getMandatoryTask() {
        return isMandatoryTask;
    }

    public void setMandatoryTask(Boolean mandatoryTask) {
        isMandatoryTask = mandatoryTask;
    }

    public DmsApi getLoadApi() {
        return loadApi;
    }

    public void setLoadApi(DmsApi loadApi) {
        this.loadApi = loadApi;
    }

    public Integer getNextTaskId() {
        return nextTaskId;
    }

    public void setNextTaskId(Integer nextTaskId) {
        this.nextTaskId = nextTaskId;
    }

    public Boolean getRepeatTask() {
        return repeatTask;
    }

    public void setRepeatTask(Boolean repeatTask) {
        this.repeatTask = repeatTask;
    }

    public com.automate.df.model.sales.workflow.Api getSubmitApi() {
        return submitApi;
    }

    public void setSubmitApi(com.automate.df.model.sales.workflow.Api submitApi) {
        this.submitApi = submitApi;
    }

    public Date getTaskCreatedTime() {
        return taskCreatedTime;
    }

    public void setTaskCreatedTime(Date taskCreatedTime) {
        this.taskCreatedTime = taskCreatedTime;
    }

    public Date getTaskEndTime() {
        return taskEndTime;
    }

    public void setTaskEndTime(Date taskEndTime) {
        this.taskEndTime = taskEndTime;
    }

    public Date getTaskExpectedEndTime() {
        return taskExpectedEndTime;
    }

    public void setTaskExpectedEndTime(Date taskExpectedEndTime) {
        this.taskExpectedEndTime = taskExpectedEndTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getTaskSequence() {
        return taskSequence;
    }

    public void setTaskSequence(Integer taskSequence) {
        this.taskSequence = taskSequence;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Date getTaskUpdateTime() {
        return taskUpdateTime;
    }

    public void setTaskUpdateTime(Date taskUpdateTime) {
        this.taskUpdateTime = taskUpdateTime;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public com.automate.df.model.sales.workflow.TaskCategory getDmsTaskCategory() {
        return dmsTaskCategory;
    }

    public void setDmsTaskCategory(com.automate.df.model.sales.workflow.TaskCategory dmsTaskCategory) {
        this.dmsTaskCategory = dmsTaskCategory;
    }

    public Organization getDmsOrganization() {
        return dmsOrganization;
    }

    public void setDmsOrganization(Organization dmsOrganization) {
        this.dmsOrganization = dmsOrganization;
    }

    public Branch getDmsBranch() {
        return dmsBranch;
    }

    public void setDmsBranch(Branch dmsBranch) {
        this.dmsBranch = dmsBranch;
    }

    public com.automate.df.model.sales.workflow.Api getDmsApi() {
        return dmsApi;
    }

    public void setDmsApi(com.automate.df.model.sales.workflow.Api dmsApi) {
        this.dmsApi = dmsApi;
    }
}


