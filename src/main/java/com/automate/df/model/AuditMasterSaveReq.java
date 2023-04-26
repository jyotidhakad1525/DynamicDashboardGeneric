package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuditMasterSaveReq {
	
	Integer orgId;
	//Integer empId;
	Integer auditWfId;
	//String auditWfName;
	boolean isEnabled;
}
