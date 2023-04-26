package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class CreateAddress {
	
	private String sno;
	String addressType;
	String houseNo;
	String street;
	String city;
	String district;
	String pincode;
	String state;
	String country;
	String village;
	String dmsLeadId;
	String latitude;
	String logitude;
	String preferredBillingAddress;
	String isRural;
	String isUrban;
	String active;
	String orgId;
	String bulkUploadId;
	String isBulkUpload;
}
