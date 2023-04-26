package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateDesignation {
	private String sno;
	String hrmsDesignationId;
	String isActive;
	String createdTime;
	String updatedTime;
	String createdBy;
	String updatedBy;
	String status;
	String approvedBy;
	String branchId;
	String departmentCode;
	String departmentName;
	String departmentId;
	String designationName;
	String level;
	String orgId;
	String bulkUploadId;
	String isBulkUpload;
}
