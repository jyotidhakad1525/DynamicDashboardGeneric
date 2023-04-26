package com.automate.df.model.salesgap;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TargetMappingAddReq {

	String branch=null;
	String location;
	String branchmangerId;
	String startDate;
	String endDate;
	String employeeId;
	String retailTarget;
	String managerId;
	String teamLeadId;
	String generalManagerId;
}
