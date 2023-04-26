package com.automate.df.model.salesgap;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TargetSearch {

	String empId;
	String empName;
	String startDate;
	String endDate;
	
	String managerId;
	String teamLeadId;
	String branchmangerId;
	String generalManagerId;
}
