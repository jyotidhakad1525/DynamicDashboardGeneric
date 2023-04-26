package com.automate.df.model;

import java.util.Date;
import java.util.Objects;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class DmsContactDto {

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
    private int ownerId;
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
    private Date title;
    private String whoDrives;

    private String relation;
    private String relationName;
    private String designation;
    private String subSource;
    
    public DmsContactDto() {
    		    }
    
    public DmsContactDto(int age, Date anniversaryDate, String annualRevenue, int branchId, String campaignId, String company, String country, String createdBy, Date createdDate, String crmAccountId, String customerType, Date dateOfBirth, String description, String email, Date enquiryDate, Integer enquirySource, String firstName, String gender, String industry, boolean isActive, String kmsTravelledInMonth, String lastName, String membersInFamily, String middleName, String modifiedBy, Date modifiedDate, String numberOfEmployees, String occupation, int orgId, int ownerId, String ownerName, String phone, String primeExpectationFromCar, String referedByFirstname, String referedByLastname, String refferedMobileNo, String refferedSource, String reffered_Sourcelocation, String salutation, String secondaryEmail, String secondaryPhone, String status, String tags, Date title, String whoDrives) {
    		        this.age = age;
    		        this.anniversaryDate = anniversaryDate;
    		        this.annualRevenue = annualRevenue;
    		        this.branchId = branchId;
    		        this.campaignId = campaignId;
    		        this.company = company;
    		        this.country = country;
    		        this.createdBy = createdBy;
    		        this.createdDate = createdDate;
    		        this.crmAccountId = crmAccountId;
    		        this.customerType = customerType;
    		        this.dateOfBirth = dateOfBirth;
    		        this.description = description;
    		        this.email = email;
    		        this.enquiryDate = enquiryDate;
    		        this.enquirySource = enquirySource;
    		        this.firstName = firstName;
    		        this.gender = gender;
    		        this.industry = industry;
    		        this.isActive = isActive;
    		        this.kmsTravelledInMonth = kmsTravelledInMonth;
    		        this.lastName = lastName;
    		        this.membersInFamily = membersInFamily;
    		        this.middleName = middleName;
    		        this.modifiedBy = modifiedBy;
    		        this.modifiedDate = modifiedDate;
    		        this.numberOfEmployees = numberOfEmployees;
    		        this.occupation = occupation;
    		        this.orgId = orgId;
    		        this.ownerId = ownerId;
    		        this.ownerName = ownerName;
    		        this.phone = phone;
    		        this.primeExpectationFromCar = primeExpectationFromCar;
    		        this.referedByFirstname = referedByFirstname;
    		        this.referedByLastname = referedByLastname;
    		        this.refferedMobileNo = refferedMobileNo;
    		        this.refferedSource = refferedSource;
    		        this.reffered_Sourcelocation = reffered_Sourcelocation;
    		        this.salutation = salutation;
    		        this.secondaryEmail = secondaryEmail;
    		        this.secondaryPhone = secondaryPhone;
    		        this.status = status;
    		        this.tags = tags;
    		        this.title = title;
    		        this.whoDrives = whoDrives;
    		    }


   
	  
}
