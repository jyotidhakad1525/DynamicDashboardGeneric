package com.automate.df.entity.sales.employee;

import com.automate.df.entity.oh.DmsBranch;
import com.automate.df.entity.oh.DmsDesignation;
import com.automate.df.entity.sales.lead.DmsAttachment;
import com.automate.df.entity.sales.master.DmsGenderMd;
import com.automate.df.entity.sales.master.DmsMaritalStatusMd;
import com.automate.df.entity.sales.master.DmsNationalityMd;
import com.automate.df.entity.sales.master.DmsReligionMd;
import com.automate.df.entity.sales.DmsOrganization;
import com.automate.df.entity.sales.workflow.DmsTaskApprover;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "dms_employee")
@NamedQuery(name = "DMSEmployee.findAll", query = "SELECT d FROM DMSEmployee d")
public class DMSEmployee implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private Integer empId;

    @Column(name = "basic_salary")
    private Integer basicSalary;

    @Column(name = "created_by")
    private String createdBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_time")
    private Date createdTime;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    private String email;

    @Column(name = "emp_name")
    private String empName;

    @Column(name = "engaged_as_id")
    private String engagedAsId;

    @Column(name = "grade_id")
    private String gradeId;

    @Column(name = "hmrs_holiday_schedule_id")
    private String hmrsHolidayScheduleId;

    @Column(name = "hrms_emp_code")
    private String hrmsEmpCode;

    @ManyToOne
    @JoinColumn(name = "approver_id")
    private DMSEmployee approverManager;

    @Column(name = "hrms_emp_id")
    private String hrmsEmpId;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_allow_overtime")
    private Boolean isAllowOvertime;

    @Column(name = "is_approval_authorities")
    private Boolean isApprovalAuthorities;

    @Column(name = "is_reporting_authorities")
    private Boolean isReportingAuthorities;

    @Column(name = "joining_date")
    private String joiningDate;

    @Column(name = "location_id")
    private String locationId;

    private String mobile;

    @Column(name = "nick_name")
    private String nickName;

    private String profession;

    @Column(name = "social_id")
    private String socialId;

    @Column(name = "sponsor_id")
    private String sponsorId;

    @Column(name = "status_id")
    private String statusId;

    @Column(name = "stores_list")
    private String storesList;

    @Column(name = "updated_by")
    private String updatedBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "updated_time")
    private Date updatedTime;

    @Column(name = "workshift_id")
    private String workshiftId;

    @Column(name = "cognito_name")
    private String cognitoName;
    
    @Column(name="old_password")
    private String oldPassword;
    
    @Column(name="new_password")
    private String newPassword;

    // bi-directional many-to-one association to DmsAttachment
    @OneToMany(mappedBy = "dmsEmployee")
    private List<DmsAttachment> dmsAttachments;

    // bi-directional many-to-one association to DmsNationalityMd
    @ManyToOne
    @JoinColumn(name = "nationality_id")
    private DmsNationalityMd dmsNationalityMd;

    // bi-directional many-to-one association to DmsEmployeeStatusMd
    @ManyToOne
    @JoinColumn(name = "employee_status_id")
    private DmsEmployeeStatusMd dmsEmployeeStatusMd;

    // bi-directional many-to-one association to DmsEmployeePayroll
    @ManyToOne
    @JoinColumn(name = "emp_payroll")
    private com.automate.df.entity.sales.employee.DmsEmployeePayroll dmsEmployeePayroll;

    // bi-directional many-to-one association to DmsEmployeePersonalInfo
    @ManyToOne
    @JoinColumn(name = "emp_personal_ifo")
    private com.automate.df.entity.sales.employee.DmsEmployeePersonalInfo dmsEmployeePersonalInfo;

    // bi-directional many-to-one association to DmsEmployeeTravel
    @ManyToOne
    @JoinColumn(name = "emp_travel")
    private DmsEmployeeTravel dmsEmployeeTravel;

    // bi-directional many-to-one association to DmsDesignation
    @ManyToOne
    @JoinColumn(name = "primary_designation")
    private DmsDesignation dmsDesignation;

    // bi-directional many-to-one association to DmsReligionMd
    @ManyToOne
    @JoinColumn(name = "religion_id")
    private DmsReligionMd dmsReligionMd;

    // bi-directional many-to-one association to DmsGenderMd
    @ManyToOne
    @JoinColumn(name = "gender_id")
    private DmsGenderMd dmsGenderMd;

    // bi-directional many-to-one association to DmsMaritalStatusMd
    @ManyToOne
    @JoinColumn(name = "marital_status_id")
    private DmsMaritalStatusMd dmsMaritalStatusMd;

    // bi-directional many-to-one association to DmsEmployee
    @ManyToOne
    @JoinColumn(name = "reporting_to")
    private DMSEmployee reportingManager;

    // bi-directional many-to-one association to DmsDepartment
    @ManyToOne
    @JoinColumn(name = "primary_department")
    private com.automate.df.entity.sales.employee.DmsDepartment dmsDepartment;

    // bi-directional many-to-one association to DmsOrganization
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "org")
    private DmsOrganization dmsOrganization;

    // bi-directional many-to-one association to DmsBranch
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "branch")
    private DmsBranch dmsBranch;

    // bi-directional many-to-one association to DmsRole
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "hrms_role")
    private DmsRole dmsRole;

    // bi-directional many-to-one association to DmsTaskApprover
    @OneToMany(mappedBy = "emp")
    private List<DmsTaskApprover> dmsTaskApprovers1;

    // bi-directional many-to-one association to DmsTaskApprover
    @OneToMany(mappedBy = "approver")
    private List<DmsTaskApprover> dmsTaskApprovers2;

    public DmsAttachment addDmsAttachment(DmsAttachment dmsAttachment) {
        getDmsAttachments().add(dmsAttachment);
        dmsAttachment.setDmsEmployee(this);

        return dmsAttachment;
    }

    public DmsAttachment removeDmsAttachment(DmsAttachment dmsAttachment) {
        getDmsAttachments().remove(dmsAttachment);
        dmsAttachment.setDmsEmployee(null);

        return dmsAttachment;
    }

}
