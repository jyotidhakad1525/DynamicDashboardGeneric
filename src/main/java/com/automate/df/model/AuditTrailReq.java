package com.automate.df.model;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuditTrailReq {
	
	String orgId;
	String empId;
	String empName;
	String stageName;
	String taskName;
	String actionType;
	
	Object beforeJson;
	Object afterJson;
	

	
}
