package com.automate.df.entity.sales.workflow;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "dms_general_task")
public class GeneralTask implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "org_id")
    private int org_id;

    @Column(name = "branch_id")
    private int branch_id;

    @Column(name = "task_name")
    private String task_name;

    @Column(name = "task_type")
    private String task_type;

    @Column(name = "task_category")
    private String task_category;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "startdate")
    private Date taskstartdate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "enddate")
    private Date taskenddate;

    @Column(name = "assigneto")
    private String assigneto;

    @Column(name = "approver")
    private String approver;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "documentLink")
    private String documentLink;

    @Column(name = "reminsder_startdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reminsder_startdate;

    @Column(name = "reminder_enddate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reminder_enddate;

    @Column(name = "reminder_frequency")
    private String reminder_frequency;

    @Column(name = "unit")
    private String unit;

    @Column(name = "task_owner")
    private String task_owner;

    @Column(name = "created_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_datetime;

    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modified_datetime;

    @Column(name = "cancelled_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cancelled_date;

    @Column(name = "status")
    private String status;

    @Column(name = "is_remainder")
    private Boolean isRemainder;

    @Column(name = "actual_startdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actualStartDate;

    @Column(name = "actaul_enddate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actaulEndDate;

    @Column(name = "customer_remarks")
    private String customerRemarks;

    @Column(name = "employee_remarks")
    private String employeeRemarks;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrg_id() {
        return org_id;
    }

    public void setOrg_id(int org_id) {
        this.org_id = org_id;
    }

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getTask_type() {
        return task_type;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }

    public String getTask_category() {
        return task_category;
    }

    public void setTask_category(String task_category) {
        this.task_category = task_category;
    }

    public Date getTaskstartdate() {
        return taskstartdate;
    }

    public void setTaskstartdate(Date taskstartdate) {
        this.taskstartdate = taskstartdate;
    }

    public Date getTaskenddate() {
        return taskenddate;
    }

    public void setTaskenddate(Date taskenddate) {
        this.taskenddate = taskenddate;
    }

    public Boolean getIsRemainder() {
        return isRemainder;
    }

    public void setIsRemainder(Boolean isRemainder) {
        this.isRemainder = isRemainder;
    }

    public String getAssigneto() {
        return assigneto;
    }

    public void setAssigneto(String assigneto) {
        this.assigneto = assigneto;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDocumentLink() {
        return documentLink;
    }

    public void setDocumentLink(String documentLink) {
        this.documentLink = documentLink;
    }

    public Date getReminsder_startdate() {
        return reminsder_startdate;
    }

    public void setReminsder_startdate(Date reminsder_startdate) {
        this.reminsder_startdate = reminsder_startdate;
    }

    public Date getReminder_enddate() {
        return reminder_enddate;
    }

    public void setReminder_enddate(Date reminder_enddate) {
        this.reminder_enddate = reminder_enddate;
    }

    public String getReminder_frequency() {
        return reminder_frequency;
    }

    public void setReminder_frequency(String reminder_frequency) {
        this.reminder_frequency = reminder_frequency;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTask_owner() {
        return task_owner;
    }

    public void setTask_owner(String task_owner) {
        this.task_owner = task_owner;
    }

    public Date getCreated_datetime() {
        return created_datetime;
    }

    public void setCreated_datetime(Date created_datetime) {
        this.created_datetime = created_datetime;
    }

    public Date getModified_datetime() {
        return modified_datetime;
    }

    public void setModified_datetime(Date modified_datetime) {
        this.modified_datetime = modified_datetime;
    }

    public Date getCancelled_date() {
        return cancelled_date;
    }

    public void setCancelled_date(Date cancelled_date) {
        this.cancelled_date = cancelled_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerRemarks() {
        return customerRemarks;
    }

    public void setCustomerRemarks(String customerRemarks) {
        this.customerRemarks = customerRemarks;
    }

    public Date getActaulEndDate() {
        return actaulEndDate;
    }

    public void setActaulEndDate(Date actaulEndDate) {
        this.actaulEndDate = actaulEndDate;
    }

    public void setActualStartDate(Date actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public String getEmployeeRemarks() {
        return employeeRemarks;
    }

    public void setEmployeeRemarks(String employeeRemarks) {
        this.employeeRemarks = employeeRemarks;
    }


}
