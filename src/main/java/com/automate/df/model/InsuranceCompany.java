package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InsuranceCompany {

	private String sno;
	private String companyName;
	private String status;
	String orgId;
	private String bulkUploadId;
	private String createdAt;
	private String createdBy;
	private String updatedAt;
	private String updatedBy;
}
