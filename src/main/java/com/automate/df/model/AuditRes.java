package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuditRes {
	
	String auditWfName;
	Integer auditWfId;
	boolean isEnabled;

}
