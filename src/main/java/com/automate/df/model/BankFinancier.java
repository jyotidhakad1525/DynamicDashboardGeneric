package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BankFinancier {

	private String sno;
	String bulkUploadId;
	String bankName;
	String bankType;
	String createdDatetime;
	String status;
	String orgId;
	String createdBy;
	String updatedBy;
	String createAt;
	String updatedAt;
	String isBulkUpload;

}
