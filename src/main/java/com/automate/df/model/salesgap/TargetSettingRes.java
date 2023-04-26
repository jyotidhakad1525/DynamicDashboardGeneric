package com.automate.df.model.salesgap;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class TargetSettingRes {

	Integer id;
	String branch;
	String branchName;
	
	//String location;
	String locationName;
	String department;
	String departmentName;
	String designation;
	String designationName;
	String experience;
	String salrayRange;
	String retailTarget;
	String enquiry;
	String testDrive;
	String homeVisit;
	String videoConference;
	String booking;
	String exchange;
	String finance;
	String insurance;
	String exWarranty;
	String accessories;
	String events;
	String invoice;
	String other;
	
	
	String startDate;
	String endDate;
	
	
	//DSE
	String empName;
	String employeeId;
	
	
	String teamLeadId;
	String teamLead;
	
	//Branch manager
	String managerId;
	String manager;
	
	//General manager
	String branchManagerId;
	String branchmanger;
	
	String generalManagerId;
	String generalManager;
	String userRole;
}
