package com.automate.df.model.sales.lead;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class DmsAccountDto {

    private int id;
    private int age;
    private Date anniversaryDate;
    private String annualRevenue;
    private int branchId;
    private String campaignId;
    private String company;
    private String country;
    private String createdBy;
    private Date createdDate;
    private String crmAccountId;
    private String customerType;
    private Date dateOfBirth;
    private String description;
    private String email;
    private Date enquiryDate;
    private Integer enquirySource;
    private String firstName;
    private String gender;
    private String industry;
    private boolean isActive;
    private String kmsTravelledInMonth;
    private String lastName;
    private String membersInFamily;
    private String middleName;
    private String modifiedBy;
    private Date modifiedDate;
    private String numberOfEmployees;
    private String occupation;
    private int orgId;
    private String ownerId;
    private String ownerName;
    private String phone;
    private String primeExpectationFromCar;
    private String referedByFirstname;
    private String referedByLastname;
    private String refferedMobileNo;
    private String refferedSource;
    private String reffered_Sourcelocation;
    private String salutation;
    private String secondaryEmail;
    private String secondaryPhone;
    private String status;
    private String tags;
    private String whoDrives;

    private String relation;
    private String relationName;
    private String designation;
    private String subSource;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

}
