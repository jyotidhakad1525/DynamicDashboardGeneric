package com.automate.df.model.sales.lead;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BulkLeadData {

    private String firstName;
    private String lastName;
    private Double mobileNumber;
    private Double alternativeNumber;
    private String email;
    private String enquirySegment;
    private String customerType;
    private String model;
    private String sourceOfEnquiry;
    private String allocateDse;
    private Double organizationId;
    private Double branchId;

    public BulkLeadData(String firstName, String lastName, Double mobileNumber, Double alternativeNumber,
                        String email, String enquirySegment, String customerType,
                        String model, String sourceOfEnquiry, String allocateDse, Double organizationId,
                        Double branchId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.alternativeNumber = alternativeNumber;
        this.email = email;
        this.enquirySegment = enquirySegment;
        this.customerType = customerType;
        this.model = model;
        this.sourceOfEnquiry = sourceOfEnquiry;
        this.allocateDse = allocateDse;
        this.organizationId = organizationId;
        this.branchId = branchId;
    }

}
