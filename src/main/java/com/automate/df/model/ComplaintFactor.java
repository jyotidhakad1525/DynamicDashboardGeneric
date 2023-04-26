package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ComplaintFactor {
	private String sno;
	String factor;
	String status;
	String orgId;
	String bulkUploadId;
	String createdBy;
	String updatedBy;
	String isBulkUpload;
}