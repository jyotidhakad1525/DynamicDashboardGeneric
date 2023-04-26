package com.automate.df.model.salesgap;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TargetMappingReq {

	Integer id;
	String userRoleId;
	String branch;
	String location;
	String branchmangerId;
	String startDate;
	String endDate;
	String employeeId;
	Integer retailTarget;
	String managerId;
	String teamLeadId;
	
}
