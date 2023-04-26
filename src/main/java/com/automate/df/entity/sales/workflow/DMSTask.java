package com.automate.df.entity.sales.workflow;

import com.automate.df.entity.sales.employee.DMSEmployee;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Setter
@Getter
@Entity
@Table(name = "dms_workflow_task")
@NamedQuery(name = "DMSTask.findAll", query = "SELECT d FROM DMSTask d")
public class DMSTask implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private int taskId;

    @Column(name = "customer_remarks")
    private String customerRemarks;

    @Column(name = "employee_remarks")
    private String employeeRemarks;

    @Column(name = "error_detail")
    private String errorDetail;

    @Column(name = "execution_job")
    private String executionJob;

    @Column(name = "is_error")
    private Boolean isError;

    @Column(name = "is_last_task")
    private Boolean isLastTask;

    @Column(name = "is_mandatory_task")
    private Boolean isMandatoryTask;

    @Column(name = "entity_status")
    private String entityStatus;

    @Column(name = "reason")
    private String reason;

    @Column(name = "repeat_task")
    private String repeatTask;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "task_actual_end_time")
    private Date taskActualEndTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "task_actual_start_time")
    private Date taskActualStartTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "task_created_time")
    private Date taskCreatedTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "task_expected_end_time")
    private Date taskExpectedEndTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "task_expected_start_time")
    private Date taskExpectedStartTime;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "task_sequence")
    private Integer taskSequence;

    @Column(name = "task_status")
    private String taskStatus;

    @Column(name = "task_type")
    private String taskType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "task_updated_time")
    private Date taskUpdatedTime;

    @Column(name = "trigger_type")
    private String triggerType;

    @Column(name = "universal_id")
    private String universalId;

    @Column(name = "is_parallel_task")
    private Boolean isParallelTask;


    //bi-directional many-to-one association to DmsWorkflowActivity
    @ManyToOne
    @JoinColumn(name = "activity_id")
    private com.automate.df.entity.sales.workflow.DMSActivity dmsActivity;

    @ManyToOne
    @JoinColumn(name = "process_id")
    private DMSProcess dmsProcess;

    //bi-directional many-to-one association to DmsEmployee
    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private DMSEmployee assignee;

    //bi-directional many-to-one association to DmsEmployee
    @ManyToOne
    @JoinColumn(name = "approver_id")
    private DMSEmployee approver;

    //bi-directional many-to-one association to DmsWorkflowTaskDef
    @ManyToOne
    @JoinColumn(name = "task_def_id")
    private com.automate.df.entity.sales.workflow.DMSTaskDef dmsTaskDef;

    //bi-directional many-to-one association to DmsApi
    @ManyToOne
    @JoinColumn(name = "api")
    private com.automate.df.entity.sales.workflow.DmsApi api;

    //bi-directional many-to-one association to DmsApi
    @ManyToOne
    @JoinColumn(name = "submit_api")
    private com.automate.df.entity.sales.workflow.DmsApi submitApi;

    @Column(name = "entity_id")
    private Integer entityId;

    @Column(name = "task_stage_status")
    private String taskStageStatus;

    //bi-directional many-to-one association to DmsTaskCategory
    @ManyToOne
    @JoinColumn(name = "task_category")
    private DMSTaskCategory dmsTaskCategory;

    @Column(name = "entity_module_id")
    private String entityModuleId;

    public Boolean getParallelTask() {
        return isParallelTask;
    }

    public void setParallelTask(Boolean parallelTask) {
        isParallelTask = parallelTask;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
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