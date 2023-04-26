package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateBranch {
	private String sno;
	String name;
	String organizationId;
	String branchType;
	String dealerCode;
	String website;
	String email;
	String phone;
	String mobile;
	String status;
	String address;
	String cinNumber;
	String storeId;
	String adress;
	String cin_number;
	String imageUrl;
	String s3Name;
	String documentUrl;
	String orgMapId;
	String active;
	private String orgId;
	private String bulkUploadId;

}
