package com.automate.df.model;



import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuditMasterRes {
	
	Integer id;
	String activityType;
	String auditWfName;
	String taskName;
	boolean isEnabled;
}
