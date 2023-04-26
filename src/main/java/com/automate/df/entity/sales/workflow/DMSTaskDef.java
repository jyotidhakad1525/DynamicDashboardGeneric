package com.automate.df.entity.sales.workflow;

import com.automate.df.entity.oh.DmsBranch;
import com.automate.df.entity.sales.DmsOrganization;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "dms_workflow_task_def")
@NamedQuery(name = "DMSTaskDef.findAll", query = "SELECT d FROM DMSTaskDef d")
public class DMSTaskDef implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_def_id")
    private int taskDefId;

    @Column(name = "execution_Job")
    private String executionJob;

    @Column(name = "is_last_task")
    private Boolean isLastTask;

    @Column(name = "is_mandatory_task")
    private Boolean isMandatoryTask;

    @Column(name = "repeat_task")
    private Boolean repeatTask;

    @Column(name = "is_parallel_task")
    private Boolean isParallelTask;

    @Column(name = "process_def_id")
    private Integer processDefId;

    @ManyToOne
    @JoinColumn(name = "submit_api")
    private com.automate.df.entity.sales.workflow.DmsApi submitApi;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "task_created_time")
    private Date taskCreatedTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "task_end_time")
    private Date taskEndTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "task_expected_end_time")
    private Date taskExpectedEndTime;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "task_sequence")
    private Integer taskSequence;

    @Column(name = "task_type")
    private String taskType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "task_update_time")
    private Date taskUpdateTime;

    @Column(name = "trigger_type")
    private String triggerType;

    @Column(name = "strategy_id")
    private Integer strategyId;

    @Column(name = "assignee_id")
    private Integer assigneeId;

    @Column(name = "approver_id")
    private Integer approverId;

    @Column(name = "escalation_role")
    private Integer escalationRole;

    // bi-directional many-to-one association to DmsWorkflowTask
    @OneToMany(mappedBy = "dmsTaskDef")
    private List<DMSTask> dmsTasks;

    // bi-directional many-to-one association to DmsTaskCategory
    @ManyToOne
    @JoinColumn(name = "task_category")
    private DMSTaskCategory dmsTaskCategory;

    // bi-directional many-to-one association to DmsOrganization
    @ManyToOne
    @JoinColumn(name = "org_id")
    private DmsOrganization dmsOrganization;

    // bi-directional many-to-one association to DmsBranch
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private DmsBranch dmsBranch;

    // bi-directional many-to-one association to DmsApi
    @ManyToOne
    @JoinColumn(name = "api")
    private com.automate.df.entity.sales.workflow.DmsApi dmsApi;

    // bi-directional many-to-one association to DmsTaskApprover
    @OneToMany(mappedBy = "dmsWorkflowTaskDef")
    private List<DmsTaskApprover> dmsTaskApprovers;

    public DMSTaskDef() {
    }

    public DMSTask addDmsWorkflowTask(DMSTask DMSTask) {
        getDmsTasks().add(DMSTask);
        DMSTask.setDmsTaskDef(this);

        return DMSTask;
    }

    public DMSTask removeDmsWorkflowTask(DMSTask DMSTask) {
        getDmsTasks().remove(DMSTask);
        DMSTask.setDmsTaskDef(null);

        return DMSTask;
    }

    public Boolean isLastTask() {
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

    public Boolean getParallelTask() {
        return isParallelTask;
    }

    public void setParallelTask(Boolean parallelTask) {
        isParallelTask = parallelTask;
    }

}