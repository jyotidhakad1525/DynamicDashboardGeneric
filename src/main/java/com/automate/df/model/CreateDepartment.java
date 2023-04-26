package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateDepartment {
	private String sno;
	String hrmsDepartmentId;
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
	String orgId;
	String bulkUploadId;
	String isBulkUpload;
}
