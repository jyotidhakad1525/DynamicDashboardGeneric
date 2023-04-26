package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ComplaintTracker {
	private String sno;
    String customerName;
    String customeLocation;
    String currentStage;
    String currentStageIdNo;
    String salesExecutiveName;
    String manager;
    String teamLeader;
    String branchManager;
    String branch;
    String complaintLocation;
    String designation;
    String employee;
    String complaintDecription;
	String mobileNo;	
	String email;
	String model;
	String complaintFactor;
    String closingSource;
    String Status;
    String orgId;
	String bulkUploadId;
	String createdBy;
	String updatedBy;
	String createdDate;
	String isBulkUpload;


}