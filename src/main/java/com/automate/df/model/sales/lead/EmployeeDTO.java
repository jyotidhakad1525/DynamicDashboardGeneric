package com.automate.df.model.sales.lead;

import com.automate.df.entity.sales.employee.DMSEmployee;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;


@Setter
@Getter
@NoArgsConstructor
public class EmployeeDTO {
    private int empId;

    private Integer basicSalary;

    private Integer branchId;

    private String createdBy;

    private Date createdTime;

    private Date dateOfBirth;

    private String designation;

    private String email;

    private String empName;

    private String employeeStatusId;

    private String engagedAsId;

    private String gradeId;

    private String hmrsHolidayScheduleId;

    private String hrmsEmpCode;

    private String hrmsEmpId;

    private int hrmsRoleId;

    private String hrmsRole;

    private String imageUrl;

    private Boolean isAllowOvertime;

    private Boolean isApprovalAuthorities;

    private Boolean isReportingAuthorities;

    private String joiningDate;

    private String locationId;

    private String mobile;

    private String nickName;

    private Integer orgId;

    private String profession;

    private String socialId;

    private String sponsorId;

    private String statusId;

    private String storesList;

    private String updatedBy;

    private Date updatedTime;

    private String workshiftId;

    private String nationality;

    private String religion;

    private String gender;

    private String maritalStatus;

    private String reportingManager;

    private int reportingTo;

    private String department;

    private int approverId;
    private String designationName;
    private String cognitoName;


    public EmployeeDTO(DMSEmployee emp) {
        this.empId = emp.getEmpId();
        this.basicSalary = emp.getBasicSalary();
        this.branchId = Objects.nonNull(emp.getDmsBranch()) ? emp.getDmsBranch().getBranchId() : 0;
        this.createdBy = emp.getCreatedBy();
        this.createdTime = emp.getCreatedTime();
        this.dateOfBirth = emp.getDateOfBirth();
        this.designation = Objects.nonNull(emp.getDmsDesignation()) ? emp.getDmsDesignation().getDesignationName() : "";
        this.designationName = Objects.nonNull(emp.getDmsDesignation()) ?
                emp.getDmsDesignation().getDesignationName() : "";
        this.email = emp.getEmail();
        this.empName = emp.getEmpName();
        this.employeeStatusId = Objects.nonNull(emp.getDmsEmployeeStatusMd()) ? emp.getDmsEmployeeStatusMd().getStatus()
                : "";
        this.engagedAsId = emp.getEngagedAsId();
        this.gradeId = emp.getGradeId();
        this.hmrsHolidayScheduleId = emp.getHmrsHolidayScheduleId();
        this.hrmsEmpCode = emp.getHrmsEmpCode();
        this.hrmsEmpId = emp.getHrmsEmpId();
        this.hrmsRole = Objects.nonNull(emp.getDmsRole()) ? emp.getDmsRole().getRoleName() : "";
        this.hrmsRoleId = Objects.nonNull(emp.getDmsRole()) ? emp.getDmsRole().getRoleId() : 0;
        this.imageUrl = emp.getImageUrl();
        this.isAllowOvertime = emp.getIsAllowOvertime();
        this.isApprovalAuthorities = emp.getIsApprovalAuthorities();
        this.isReportingAuthorities = emp.getIsReportingAuthorities();
        this.joiningDate = emp.getJoiningDate();
        this.locationId = emp.getLocationId();
        this.mobile = emp.getMobile();
        this.nickName = emp.getNickName();
        this.orgId = Objects.nonNull(emp.getDmsOrganization()) ? emp.getDmsOrganization().getOrgId() : 0;
        this.profession = emp.getProfession();
        this.socialId = emp.getSocialId();
        this.sponsorId = emp.getSponsorId();
        this.statusId = emp.getStatusId();
        this.storesList = emp.getStoresList();
        this.updatedBy = emp.getUpdatedBy();
        this.updatedTime = emp.getUpdatedTime();
        this.workshiftId = emp.getWorkshiftId();
        if (Objects.nonNull(emp.getReportingManager())) {
            // this.reportingManager = new EmployeeDTO(emp.getReportingManager());
            this.reportingTo = emp.getReportingManager().getEmpId();
            this.reportingManager = emp.getReportingManager().getEmpName();
        }
        this.nationality = Objects.nonNull(emp.getDmsNationalityMd()) ? emp.getDmsNationalityMd().getName() : "";
        this.religion = Objects.nonNull(emp.getDmsReligionMd()) ? emp.getDmsReligionMd().getName() : "";
        this.gender = Objects.nonNull(emp.getDmsGenderMd()) ? emp.getDmsGenderMd().getName() : "";
        this.maritalStatus = Objects.nonNull(emp.getDmsMaritalStatusMd()) ? emp.getDmsMaritalStatusMd().getStatus()
                : "";
        this.department = Objects.nonNull(emp.getDmsDepartment()) ? emp.getDmsDepartment().getDepartmentName() : "";
        this.approverId = Objects.nonNull(emp.getApproverManager()) ? emp.getApproverManager().getEmpId() : 0;
        this.cognitoName = emp.getCognitoName();
    }

}
