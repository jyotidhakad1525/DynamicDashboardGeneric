package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LostReasons {
	private String sno;
	String stageName;
	String lostReason;
	String status;
	String orgId;
	String bulkUploadId;
	String createdBy;
	String updatedBy;
	String isBulkUpload;
}
