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
@Table(name = "dms_workflow_activity")
@NamedQuery(name = "DMSActivity.findAll", query = "SELECT d FROM DMSActivity d")
public class DMSActivity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private int activityId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "activity_actual_end_time")
    private Date activityActualEndTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "activity_actual_start_time")
    private Date activityActualStartTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "activity_created_time")
    private Date activityCreatedTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "activity_expected_end_time")
    private Date activityExpectedEndTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "activity_expected_start_time")
    private Date activityExpectedStartTime;

    @Column(name = "activity_name")
    private String activityName;

    @Column(name = "activity_sequence")
    private Integer activitySequence;

    @Column(name = "activity_status")
    private String activityStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "activity_update_time")
    private Date activityUpdateTime;

    @Column(name = "approver_id")
    private String approverId;

    @Column(name = "assignee_id")
    private String assigneeId;

    @Column(name = "error_detail")
    private String errorDetail;

    @Column(name = "execution_job")
    private String executionJob;

    @Column(name = "is_error")
    private Boolean isError;

    @Column(name = "is_last_task")
    private Boolean isLastTask;

    @Column(name = "is_mandatory_activity")
    private Boolean isMandatoryActivity;

    @Column(name = "entity_status")
    private String entityStatus;

    @Column(name = "next_activity_id")
    private Integer nextActivityId;

    @Column(name = "repeat_activity")
    private Boolean repeatActivity;

    @Column(name = "success_state")
    private Boolean successState;

    @Column(name = "trigger_type")
    private String triggerType;

    @Column(name = "universal_id")
    private String universalId;

    //bi-directional many-to-one association to DmsWorkflowProcess
    @ManyToOne
    @JoinColumn(name = "process_id")
    private DMSProcess dmsProcess;

    //bi-directional many-to-one association to DmsWorkflowActivityDef
    @ManyToOne
    @JoinColumn(name = "activity_def_id")
    private DMSActivityDef activityDef;

    //bi-directional many-to-one association to DmsWorkflowTask
    @OneToMany(mappedBy = "dmsActivity")
    private List<DMSTask> tasks;
    @Column(name = "entity_id")
    private Integer entityId;

    public DMSTask addDmsWorkflowTask(DMSTask DMSTask) {
        getTasks().add(DMSTask);
        DMSTask.setDmsActivity(this);

        return DMSTask;
    }

    public DMSTask removeDmsWorkflowTask(DMSTask DMSTask) {
        getTasks().remove(DMSTask);
        DMSTask.setDmsActivity(null);

        return DMSTask;
    }

}