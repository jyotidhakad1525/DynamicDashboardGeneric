package com.automate.df.entity.salesgap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="dms_target_setting")
@Entity
@Data
@NoArgsConstructor
public class TargetEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="org_id")
	String orgId;
	@Column(name="branch")
	String branch;
	//String location;
	String department;
	String designation;
	String experience;
	
	
	@Column(name="salary_range")
	String salrayRange;
	
	@Column(name="min_salary")
	String minSalary;
	
	@Column(name="max_salary")
	String maxSalary;
	
	@Column(name="targets")
	String targets;
	
	
	/*
	@Column(name="retail_target")
	Integer retailTarget;
	Integer enquiry;
	@Column(name="test_drive")
	Integer testDrive;
	@Column(name="home_visit")
	Integer homeVisit;
	@Column(name="video_conference")
	Integer videoConference;
	Integer booking;
	Integer exchange;
	Integer finance;
	Integer insurance;
	@Column(name="ex_warranty")
	Integer exWarranty;
	Integer accessories;
	Integer events;
	*/
	
	@Column(name="user_role_id")
	String userRoleId;
	
	

	@Column(name="retail_target")
	Integer retailTarget;

	
	@Column(name="start_date")
	String startDate;
	@Column(name="end_date")
	String endDate;
	
	
	//DSE
	@Column(name="emp_name")
	String empName;
	@Column(name="emp_id")
	String employeeId;
	
	
	//Manager
	@Column(name="team_lead")
	String teamLeadId;
	
	//Branch manager
	@Column(name="manager")
	String managerId;
	
	//General manager
	@Column(name="branch_manager")
	String branchmangerId;
	
	//General manager
	@Column(name="general_manager")
	String generalManager;
	
	@Column(name="is_active")
	String active;
}
