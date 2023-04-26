package com.automate.df.entity.salesgap;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="dms_employee")
@Entity
@Data
@NoArgsConstructor
public class DmsEmployee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="emp_id")
	private int emp_id;
	
	@Column(name="hrms_emp_id")
	private String hrmsEmpId;
	
	@Column(name="hrms_emp_code")
	private String hrmsEmpCode;
	
	@Column(name="emp_name")
	private String empName;
	
	@Column(name="basic_salary")
	private String basicSal;
	
	@Column(name="reporting_to")
	private String reportingTo;
	
	@Column(name="primary_department")
	private String deptId;
	
	@Column(name="primary_designation")
	private String designationId;
	
	
	@Column(name="employee_status_id")
	private String statusId;
	
	@Column(name="sponsor_id")
	private String sponserId;
	
	@Column(name="hrms_role")
	private String hrmsRole;
	
	//grade_id
	//hrms_role
	//hmrs_holiday_schedule_id
	@Column(name="joining_date")
	private String joiningDate;

	@Column(name="prev_experience")
	private String prevExperience;
	
	//workshift_id
	//stores_list
	//image_url
	//social_id
	@Column(name="location")
	private String locationId;
	
	//profession
//	status_id
	//is_allow_overtime
	//is_approval_authorities
	//is_reporting_authorities
	//org
	//branch
	//created_time
	//updated_time
	//created_by
	//updated_by
	
	@Column(name="branch")
	private String branch;
	
	@Column(name="org")
	private String org;
	
	@Column(name="emp_payroll")
	private String empPayrollId;
	
	@Column(name="cognito_name")
	private String cogintoName;
	

	
	/*emp_personal_ifo
	emp_travel
	approver_id
	cognito_name
	dashboard_url
	status
	s3_name
	address*/
}
