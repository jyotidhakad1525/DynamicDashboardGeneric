package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ETVPreEnquiry {
	
	String location;
	String dealerCode;
	String preEnqId;
	String preEnqDate;
	String preEnqMonthYear;
	String firstName;
	String lastName;
	String mobileNo;
	String emailId;
	String model;
	String enqSegment;
	String customerType;
	String sourceOfPreEnquiry;
	String subSoruceOfPreEnquiry;
	String pincode;
	String dropDate;
	String dropReason;
	String subDropReason;
	String assignedBy;
	String salesExecutive;
	String salesExecutiveEmpId;
	String teamLead;
	String manager;
	String remarks;

}
