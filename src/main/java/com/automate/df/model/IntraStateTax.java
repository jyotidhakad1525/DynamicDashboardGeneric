package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IntraStateTax {
	private String sno;
	String unit;
	String cgst;
	String sgst;
	String cess;
	String total;
	String status;
	String orgId;
	String bulkUploadId;
	String createdBy;
	String updatedBy;
	String isBulkUpload;
	String fuelType;


}
