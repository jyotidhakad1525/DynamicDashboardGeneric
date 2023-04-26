package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubSource {
	private String sno;
	String source;
	String subSource;
	String orgId;
	String bulkUploadId;
	String createdBy;
	String updatedBy;
	String status;
	String isBulkUpload;
	String sourceId;
}