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
@Table(name = "dms_workflow_process_def")
@NamedQuery(name = "DMSProcessDef.findAll", query = "SELECT d FROM DMSProcessDef d")
public class DMSProcessDef implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "process_def_id")
    private int processDefId;

    @Column(name = "execution_job")
    private String executionJob;

    @Column(name = "is_last_task")
    private Boolean isLastTask;

    @Column(name = "is_mandatory_process")
    private Boolean isMandatoryProcess;

    @Column(name = "next_process_id")
    private Integer nextProcessId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "process_created_time")
    private Date processCreatedTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "process_end_time")
    private Date processEndTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "process_expected_end_time")
    private Date processExpectedEndTime;

    @Column(name = "process_name")
    private String processName;

    @Column(name = "process_sequence")
    private Integer processSequence;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "process_updated_time")
    private Date processUpdatedTime;

    @Column(name = "repeat_process")
    private Boolean repeatProcess;

    @Column(name = "trigger_type")
    private String triggerType;

    @Column(name = "workflow_def_id")
    private Integer workflowDefId;

    //bi-directional many-to-one association to DmsWorkflowProcess
    @OneToMany(mappedBy = "dmsProcessDef")
    private List<DMSProcess> DMSProcesses;

    //bi-directional many-to-one association to DmsOrganization
    @ManyToOne
    @JoinColumn(name = "org_id")
    private DmsOrganization dmsOrganization;

    //bi-directional many-to-one association to DmsBranch
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private DmsBranch dmsBranch;

    public DMSProcess addDmsWorkflowProcess(DMSProcess DMSProcess) {
        getDMSProcesses().add(DMSProcess);
        DMSProcess.setDmsProcessDef(this);

        return DMSProcess;
    }

    public DMSProcess removeDmsWorkflowProcess(DMSProcess DMSProcess) {
        getDMSProcesses().remove(DMSProcess);
        DMSProcess.setDmsProcessDef(null);

        return DMSProcess;
    }

}