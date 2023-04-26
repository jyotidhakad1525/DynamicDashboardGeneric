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
@Table(name = "dms_workflow_def")
@NamedQuery(name = "DMSWorkflowDef.findAll", query = "SELECT d FROM DMSWorkflowDef d")
public class DMSWorkflowDef implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workflow_def_id")
    private int workflowDefId;

    @Column(name = "execution_job")
    private String executionJob;

    @Column(name = "is_last_task")
    private Boolean isLastTask;

    @Column(name = "repeat_process")
    private Boolean repeatProcess;

    @Column(name = "trigger_type")
    private String triggerType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "workflow_created_time")
    private Date workflowCreatedTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "workflow_end_time")
    private Date workflowEndTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "workflow_expected_end_time")
    private Date workflowExpectedEndTime;

    @Column(name = "workflow_name")
    private String workflowName;

    @Column(name = "workflow_sequence")
    private Integer workflowSequence;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "workflow_updated_time")
    private Date workflowUpdatedTime;

    //bi-directional many-to-one association to DmsWorkflow
    @OneToMany(mappedBy = "dmsWorkflowDef")
    private List<DMSWorkflow> DMSWorkflows;

    //bi-directional many-to-one association to DmsOrganization
    @ManyToOne
    @JoinColumn(name = "org_id")
    private DmsOrganization dmsOrganization;

    //bi-directional many-to-one association to DmsBranch
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private DmsBranch dmsBranch;

    public DMSWorkflow addDmsWorkflow(DMSWorkflow dmsWorkflow) {
        getDMSWorkflows().add(dmsWorkflow);
        dmsWorkflow.setDmsWorkflowDef(this);

        return dmsWorkflow;
    }

    public DMSWorkflow removeDmsWorkflow(DMSWorkflow dmsWorkflow) {
        getDMSWorkflows().remove(dmsWorkflow);
        dmsWorkflow.setDmsWorkflowDef(null);

        return dmsWorkflow;
    }


}