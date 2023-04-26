package com.automate.df.entity.sales.workflow;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "dms_workflow_process")
@NamedQuery(name = "DMSProcess.findAll", query = "SELECT d FROM DMSProcess d")
public class DMSProcess implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "process_id")
    private int processId;

    @Column(name = "approver_id")
    private String approverId;

    @Column(name = "assignee_id")
    private String assigneeId;

    @Column(name = "error_detail")
    private String errorDetail;

    @Column(name = "is_error")
    private Boolean isError;

    @Column(name = "is_last_task")
    private Boolean isLastTask;

    @Column(name = "is_mandatory_process")
    private Boolean isMandatoryProcess;

    @Column(name = "entity_status")
    private String entityStatus;

    @Column(name = "next_process_id")
    private Integer nextProcessId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "process_actual_end_time")
    private Date processActualEndTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "process_actual_start_time")
    private Date processActualStartTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "process_created_time")
    private Date processCreatedTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "process_expected_end_time")
    private Date processExpectedEndTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "process_expected_start_time")
    private Date processExpectedStartTime;

    @Column(name = "process_name")
    private String processName;

    @Column(name = "process_sequence")
    private Integer processSequence;

    @Column(name = "process_status")
    private String processStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "process_updated_time")
    private Date processUpdatedTime;

    @Column(name = "repeat_process")
    private Boolean repeatProcess;

    @Column(name = "success_state")
    private Boolean successState;

    @Column(name = "universal_id")
    private String universalId;

    //bi-directional many-to-one association to DmsWorkflowActivity
    @OneToMany(mappedBy = "dmsProcess")
    private List<com.automate.df.entity.sales.workflow.DMSActivity> dmsWorkflowActivities;

    //bi-directional many-to-one association to DmsWorkflow
    @ManyToOne
    @JoinColumn(name = "workflow_id")
    private com.automate.df.entity.sales.workflow.DMSWorkflow dmsWorkflow;

    @OneToMany(mappedBy = "dmsProcess")
    private List<com.automate.df.entity.sales.workflow.DMSTask> tasks;

    //bi-directional many-to-one association to DmsWorkflowProcessDef
    @ManyToOne
    @JoinColumn(name = "process_def_id")
    private com.automate.df.entity.sales.workflow.DMSProcessDef dmsProcessDef;
    @Column(name = "entity_id")
    private Integer entityId;

    public com.automate.df.entity.sales.workflow.DMSActivity addDmsWorkflowActivity(com.automate.df.entity.sales.workflow.DMSActivity DMSActivity) {
        getDmsWorkflowActivities().add(DMSActivity);
        DMSActivity.setDmsProcess(this);

        return DMSActivity;
    }

    public com.automate.df.entity.sales.workflow.DMSActivity removeDmsWorkflowActivity(com.automate.df.entity.sales.workflow.DMSActivity DMSActivity) {
        getDmsWorkflowActivities().remove(DMSActivity);
        DMSActivity.setDmsProcess(null);

        return DMSActivity;
    }

}