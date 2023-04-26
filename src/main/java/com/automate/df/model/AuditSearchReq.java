package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuditSearchReq {
	
	String leadId;
	String universalId;
	String empId;
	String stage;
	String startDate;
	String endDate;

}
