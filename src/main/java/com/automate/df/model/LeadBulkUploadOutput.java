package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LeadBulkUploadOutput {
	
	Integer orgId;
	String branch;
	String location;
	String firstName;
	String lastName;
	String mobileNo;
	String alternateMobileNo;
	String email;
	String Model;
	String segment;
	String customerType;
	String enqSource;
	String subSource;
	String pinCode;
	String errorMsg;

}
