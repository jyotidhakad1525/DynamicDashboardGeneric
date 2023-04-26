package com.automate.df.model.sales.lead;

import com.automate.df.entity.sales.workflow.DmsTaskApprover;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DmsTaskApproverModel {

    private int empid;

    private String empName;

    private String approverName;

    private int approverId;

    private int taskDefId;

    private String taskType;

    private String taskName;

    private int id;

    private int empDepartmentId;

    private int empDesignationId;

    private int approverGrade;

    private int approverDepartmentId;

    private int approverDesignationId;

    private String approverDepartmentName;

    private String approverDesignationName;

    private String empDepartmentName;

    private String empDesignationName;

    private int empGrade;

    public DmsTaskApproverModel() {

    }

    public DmsTaskApproverModel(DmsTaskApprover task) {
        this.empid = task.getEmp().getEmpId();
        this.empName = task.getEmp().getEmpName();
        this.approverName = task.getApprover().getEmpName();
        this.approverId = task.getApprover().getEmpId();
        this.taskDefId = task.getDmsWorkflowTaskDef().getTaskDefId();
        this.taskType = task.getDmsWorkflowTaskDef().getTaskType();
        this.taskName = task.getDmsWorkflowTaskDef().getTaskName();
        this.empDepartmentId = task.getEmp().getDmsDepartment().getDmsDepartmentId();
        this.empDesignationId = task.getEmp().getDmsDesignation().getDmsDesignationId();
        this.empDepartmentName = task.getEmp().getDmsDepartment().getDepartmentName();
        this.empDesignationName = task.getEmp().getDmsDesignation().getDesignationName();
        this.empGrade = task.getEmp().getDmsDesignation().getLevel();
        this.approverDepartmentId = task.getApprover().getDmsDepartment().getDmsDepartmentId();
        this.approverDepartmentName = task.getApprover().getDmsDepartment().getDepartmentName();
        this.approverDesignationId = task.getApprover().getDmsDesignation().getDmsDesignationId();
        this.approverDesignationName = task.getApprover().getDmsDesignation().getDesignationName();
        this.approverGrade = task.getApprover().getDmsDesignation().getLevel();
        this.id = task.getId();
    }

}
