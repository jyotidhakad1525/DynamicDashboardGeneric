package com.automate.df.model.df.dashboard;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DashBoardReqV2 {
	
	Integer loggedInEmpId;
	List<Integer> empSelected;
	List<Integer> levelSelected;
	
	/*
	List<String> employeeIds;
	List<String> branchIds;
	List<String> vpIds;
	List<String> generalMgrId;
	List<String> managerIds;
	List<String> teamLeadIds;
	*/
	
	
	String startDate;
	String endDate;
	
	String branchSelectionInEvents;
	int pageNo;
	int size;
}
