package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EnquirySegment {

	private String sno;
	String enquirySegment;
	String status;
	String orgId;
	String bulkUploadId;
	String createdBy;
	String updatedBy;
	String isBulkUpload;
}