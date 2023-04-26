package com.automate.df.model.df.dashboard;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DashBoardReq {
	
	String loggedInEmpId;
	String employeeId;
	String branchId;
	String locationId;
	String branchmangerId;
	String managerId;
	String teamLeadId;
	String startDate;
	String endDate;
	
	String branchSelectionInEvents;
	int pageNo;
	int size;
	
}
