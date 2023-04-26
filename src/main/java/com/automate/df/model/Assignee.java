package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Assignee {
	public int empId;
    public String basicSalary="";
    public int branchId;
    public String createdBy="";
    public String createdTime="";
    public String dateOfBirth="";
    public String designation="";
    public String email="";
    public String empName="";
    public String employeeStatusId="";
    public String engagedAsId="";
    public String gradeId="";
    public String hmrsHolidayScheduleId="";
    public String hrmsEmpCode="";
    public String hrmsEmpId="";
    public int hrmsRoleId;
    public String hrmsRole="";
    public String imageUrl="";
    public String isAllowOvertime="";
    public String isApprovalAuthorities="";
    public String isReportingAuthorities="";
    public String joiningDate="";
    public String locationId="";
    public String mobile="";
    public String nickName="";
    public int orgId;
    public String profession="";
    public String socialId="";
    public String sponsorId="";
    public String statusId="";
    public String storesList="";
    public String updatedBy="";
    public String updatedTime="";
    public String workshiftId="";
    public String nationality="";
    public String religion="";
    public String gender="";
    public String maritalStatus="";
    public String reportingManager="";
    public int reportingTo;
    public String department="";
    public int approverId;
    public String designationName="";
    public String cognitoName="";
}
