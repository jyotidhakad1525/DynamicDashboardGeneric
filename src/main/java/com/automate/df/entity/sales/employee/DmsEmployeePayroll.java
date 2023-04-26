package com.automate.df.entity.sales.employee;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "dms_employee_payroll")
@NamedQuery(name = "DmsEmployeePayroll.findAll", query = "SELECT d FROM DmsEmployeePayroll d")
public class DmsEmployeePayroll implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dms_employee_payroll_id")
    private int dmsEmployeePayrollId;

    @Column(name = "alternative_number")
    private String alternativeNumber;

    @Column(name = "bank_account")
    private String bankAccount;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "basic_salary")
    private int basicSalary;

    @Column(name = "bio_metric_id")
    private String bioMetricId;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "created_by")
    private String createdBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "esi_number")
    private String esiNumber;

    @Column(name = "gross_salary")
    private int grossSalary;

    @Column(name = "is_loan_eligable")
    private Boolean isLoanEligable;

    @Column(name = "loan_amount_limit")
    private int loanAmountLimit;

    @Column(name = "max_pf_deduct_amount")
    private int maxPfDeductAmount;

    @Column(name = "on_exited")
    private String onExited;

    @Column(name = "on_hold")
    private String onHold;

    @Column(name = "pan_or_tan_id")
    private String panOrTanId;

    @Column(name = "payroll_group_id")
    private String payrollGroupId;

    @Column(name = "pf_number")
    private String pfNumber;

    @Column(name = "reference_by")
    private String referenceBy;

    @Column(name = "source_walkin")
    private String sourceWalkin;

    @Column(name = "updated_by")
    private String updatedBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "updated_time")
    private Date updatedTime;

    //bi-directional many-to-one association to DmsEmployee
    @OneToMany(mappedBy = "dmsEmployeePayroll")
    private List<DMSEmployee> DMSEmployees;

    public DMSEmployee addDmsEmployee(DMSEmployee dmsEmployee) {
        getDMSEmployees().add(dmsEmployee);
        dmsEmployee.setDmsEmployeePayroll(this);

        return dmsEmployee;
    }

    public DMSEmployee removeDmsEmployee(DMSEmployee dmsEmployee) {
        getDMSEmployees().remove(dmsEmployee);
        dmsEmployee.setDmsEmployeePayroll(null);

        return dmsEmployee;
    }

}