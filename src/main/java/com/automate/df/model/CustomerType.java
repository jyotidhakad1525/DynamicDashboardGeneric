package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerType {
	
	private String sno;
	String customerType;
	String enquirySegment;
	String orgId;
	String bulkUploadId;
	String createdBy;
	String updatedBy;
	String status;
	String isBulkUpload;

}
