package com.automate.df.entity.sales.workflow;

import com.automate.df.entity.oh.DmsBranch;
import com.automate.df.entity.sales.DmsOrganization;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Setter
@Getter
@Entity
@Table(name = "dms_workflow_activity_def")
@NamedQuery(name = "DMSActivityDef.findAll", query = "SELECT d FROM DMSActivityDef d")
public class DMSActivityDef implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_def_id")
    private int activityDefId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "activity_created_time")
    private Date activityCreatedTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "activity_end_time")
    private Date activityEndTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "activity_expected_end_time")
    private Date activityExpectedEndTime;

    @Column(name = "activity_name")
    private String activityName;

    @Column(name = "activity_sequence")
    private Integer activitySequence;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "activity_update_time")
    private Date activityUpdateTime;

    @Column(name = "execution_job")
    private String executionJob;

    @Column(name = "is_last_activity")
    private Boolean isLastActivity;

    @Column(name = "is_mandatory_activity")
    private Boolean isMandatoryActivity;

    @Column(name = "next_activity_id")
    private Integer nextActivityId;

    @Column(name = "process_def_id")
    private Integer processDefId;

    @Column(name = "repeat_process")
    private Boolean repeatProcess;

    @Column(name = "trigger_type")
    private String triggerType;


    //bi-directional many-to-one association to DmsOrganization
    @ManyToOne
    @JoinColumn(name = "org_id")
    private DmsOrganization dmsOrganization;

    //bi-directional many-to-one association to DmsBranch
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private DmsBranch dmsBranch;

    public void setActivityDefId(int activityDefId) {
        this.activityDefId = activityDefId;
    }

    public Boolean getLastActivity() {
        return isLastActivity;
    }

    public void setLastActivity(Boolean lastActivity) {
        isLastActivity = lastActivity;
    }

    public Boolean getMandatoryActivity() {
        return isMandatoryActivity;
    }

    public void setMandatoryActivity(Boolean mandatoryActivity) {
        isMandatoryActivity = mandatoryActivity;
    }
}