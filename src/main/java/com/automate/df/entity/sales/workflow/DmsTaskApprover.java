package com.automate.df.entity.sales.workflow;

import com.automate.df.entity.oh.DmsBranch;
import com.automate.df.entity.sales.DmsOrganization;
import com.automate.df.entity.sales.employee.DMSEmployee;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Setter
@Getter
@Entity
@Table(name = "dms_task_approver")
@NamedQuery(name = "DmsTaskApprover.findAll", query = "SELECT d FROM DmsTaskApprover d")
public class DmsTaskApprover implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private int id;

    // bi-directional many-to-one association to DmsEmployee
    @ManyToOne
    @JoinColumn(name = "empid")
    private DMSEmployee emp;

    // bi-directional many-to-one association to DmsEmployee
    @ManyToOne
    @JoinColumn(name = "approverid")
    private DMSEmployee approver;

    // bi-directional many-to-one association to DmsWorkflowTaskDef
    @ManyToOne
    @JoinColumn(name = "taskdefid")
    private com.automate.df.entity.sales.workflow.DMSTaskDef dmsWorkflowTaskDef;

    // bi-directional many-to-one association to DmsOrganization
    @ManyToOne
    @JoinColumn(name = "orgid")
    private DmsOrganization dmsOrganization;

    // bi-directional many-to-one association to DmsBranch
    @ManyToOne
    @JoinColumn(name = "branchid")
    private DmsBranch dmsBranch;

}