package com.automate.df.entity.sales.workflow;

import com.automate.df.entity.sales.employee.DmsDepartment;
import com.automate.df.entity.sales.employee.DmsRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "dms_task_category")
@NamedQuery(name = "DMSTaskCategory.findAll", query = "SELECT d FROM DMSTaskCategory d")
public class DMSTaskCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "created_date")
    private String createdDate;

    private String description;

    @Column(name = "execution_type")
    private String executionType;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "task_category")
    private String taskCategory;

    @Column(name = "task_type")
    private String taskType;

    @Column(name = "approval_type")
    private Boolean approvalType;

    //bi-directional many-to-one association to DmsApi
    @ManyToOne
    @JoinColumn(name = "task_api")
    private com.automate.df.entity.sales.workflow.DmsApi dmsApi;


    //bi-directional many-to-one association to DmsRole
    @ManyToOne
    @JoinColumn(name = "assignee_role")
    private DmsRole assigneeRole;

    //bi-directional many-to-one association to DmsRole
    @ManyToOne
    @JoinColumn(name = "approver_role")
    private DmsRole approverRole;

    //bi-directional many-to-one association to DmsRole
    @ManyToOne
    @JoinColumn(name = "escalation_role")
    private DmsRole escalationRole;

    //bi-directional many-to-one association to DmsDepartment
    @ManyToOne
    @JoinColumn(name = "assignee_dept")
    private DmsDepartment dmsDepartment;

    //bi-directional many-to-one association to DmsWorkflowTaskDef
    @OneToMany(mappedBy = "dmsTaskCategory")
    private List<com.automate.df.entity.sales.workflow.DMSTaskDef> DMSTaskDefs;

    public com.automate.df.entity.sales.workflow.DMSTaskDef addDmsWorkflowTaskDef(com.automate.df.entity.sales.workflow.DMSTaskDef DMSTaskDef) {
        getDMSTaskDefs().add(DMSTaskDef);
        DMSTaskDef.setDmsTaskCategory(this);

        return DMSTaskDef;
    }

    public com.automate.df.entity.sales.workflow.DMSTaskDef removeDmsWorkflowTaskDef(com.automate.df.entity.sales.workflow.DMSTaskDef DMSTaskDef) {
        getDMSTaskDefs().remove(DMSTaskDef);
        DMSTaskDef.setDmsTaskCategory(null);

        return DMSTaskDef;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }


}