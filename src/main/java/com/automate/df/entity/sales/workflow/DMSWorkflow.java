package com.automate.df.entity.sales.workflow;

import com.automate.df.entity.sales.employee.DMSEmployee;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "dms_workflow")
@NamedQuery(name = "DMSWorkflow.findAll", query = "SELECT d FROM DMSWorkflow d")
public class DMSWorkflow implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workflow_id")
    private int workflowId;

    @Column(name = "current_activity")
    private Integer currentActivity;

    @Column(name = "current_process")
    private Integer currentProcess;

    @Column(name = "error_detail")
    private String errorDetail;

    @Column(name = "is_error")
    private Boolean isError;

    @Column(name = "is_last_task")
    private Boolean isLastTask;

    @Column(name = "last_activity_completed")
    private Integer lastActivityCompleted;

    @Column(name = "last_process_completed")
    private Integer lastProcessCompleted;

    @Column(name = "last_task_completed")
    private Integer lastTaskCompleted;

    @Column(name = "entity_status")
    private String entityStatus;

    @Column(name = "repeat_workflow")
    private Boolean repeatWorkflow;

    @Column(name = "success_state")
    private Boolean successState;

    @Column(name = "universal_id")
    private String universalId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "workflow_actual_end_time")
    private Date workflowActualEndTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "workflow_actual_start_time")
    private Date workflowActualStartTime;

    @Column(name = "workflow_created_by")
    private Integer workflowCreatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "workflow_created_time")
    private Date workflowCreatedTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "workflow_expected_end_time")
    private Date workflowExpectedEndTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "workflow_expected_start_time")
    private Date workflowExpectedStartTime;

    @Column(name = "workflow_name")
    private String workflowName;

    @Column(name = "workflow_sequence")
    private Integer workflowSequence;

    @Column(name = "workflow_status")
    private String workflowStatus;

    @Column(name = "workflow_updated_by")
    private Integer workflowUpdatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "workflow_updated_time")
    private Date workflowUpdatedTime;

    //bi-directional many-to-one association to DmsWorkflowDef
    @ManyToOne
    @JoinColumn(name = "workflow_def_id")
    private com.automate.df.entity.sales.workflow.DMSWorkflowDef dmsWorkflowDef;

    //bi-directional many-to-one association to DmsEmployee
    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private DMSEmployee assignee;

    //bi-directional many-to-one association to DmsEmployee
    @ManyToOne
    @JoinColumn(name = "approver_id")
    private DMSEmployee approver;

    //bi-directional many-to-one association to DmsWorkflowProcess
    @OneToMany(mappedBy = "dmsWorkflow")
    private List<DMSProcess> dmsProcesses;
    @Column(name = "entity_id")
    private Integer entityId;

    public DMSProcess addDmsWorkflowProcess(DMSProcess DMSProcess) {
        getDmsProcesses().add(DMSProcess);
        DMSProcess.setDmsWorkflow(this);

        return DMSProcess;
    }

    public DMSProcess removeDmsWorkflowProcess(DMSProcess DMSProcess) {
        getDmsProcesses().remove(DMSProcess);
        DMSProcess.setDmsWorkflow(null);

        return DMSProcess;
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

}