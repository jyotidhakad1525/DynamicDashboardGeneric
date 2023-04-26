package com.automate.df.model.salesgap;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TargetSettingReq {

	String orgId;
	String branch;
	//String location;
	String department;
	String designation;
	String experience;
	String salrayRange;
	
	List<Target> targets;
	
	/*Integer retailTarget;
	Integer enquiry;
	Integer tesDrive;
	Integer homeVisit;
	Integer videoConference;
	Integer booking;
	Integer exchange;
	Integer finance;
	Integer insurance;
	Integer exWarranty;
	Integer accessories;
	Integer events;
	
	
	
	String startDate;
	String endDate;
	
	
	//DSE
	String empName;
	String empId;
	
	
	//Manager
	String teamLead;
	
	//Branch manager
	
	String manager;
	
	//General manager
	String branchManager;
	 */
}
