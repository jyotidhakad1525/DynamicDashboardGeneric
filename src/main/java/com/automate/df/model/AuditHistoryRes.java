package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuditHistoryRes {

	String leadId;
	String universalId;
	String fieldName;
	String oldValue;
	String updatedValue;
	String updatedEmpId;
	String updatedEmpName;
	String updatedAt;
}
