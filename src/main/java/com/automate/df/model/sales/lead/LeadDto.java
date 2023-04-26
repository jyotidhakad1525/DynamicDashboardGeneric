package com.automate.df.model.sales.lead;

import com.automate.df.entity.dashboard.DmsLead;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@NoArgsConstructor
@Setter
@Getter
public class LeadDto {

    private String universalId;

    private int leadId;

    private String firstName;

    private String lastName;

    private Date createdDate;

    private Date dateOfBirth;

    private String enquirySource;

    private Date enquiryDate;
    
    private Date commitmentDeliveryPreferredDate;
    
    private String chassisNo;
    
    private String deliveryLocation;

    private String model;

    private String enquirySegment;

    private String phone;

    private String leadStage;

    private String customerType;

    private String alternativeNumber;

    private String enquiryCategory;

    private String createdBy;

    private String salesConsultant;

    private String email;
    private String leadStatus;
    private String referencenumber;

    public LeadDto(DmsLead lead) {
        if (Objects.nonNull(lead.getDmsAccount())) {
            this.createdDate = lead.getCreateddatetime();
            this.dateOfBirth = lead.getDmsAccount().getDateOfBirth();
            this.alternativeNumber = lead.getDmsAccount().getSecondaryPhone();
            this.customerType = lead.getDmsAccount().getCustomerType();

        } else if (Objects.nonNull(lead.getDmsContact())) {
            this.createdDate = lead.getCreateddatetime();
            this.dateOfBirth = lead.getDmsContact().getDateOfBirth();
            this.alternativeNumber = lead.getDmsContact().getSecondaryPhone();
            this.customerType = lead.getDmsContact().getCustomerType();
        }
        this.firstName = lead.getFirstName();
        this.lastName = lead.getLastName();
        this.createdDate = lead.getCreateddatetime();
        this.phone = lead.getPhone();
        this.leadId = lead.getId();
        this.universalId = lead.getCrmUniversalId();
        this.enquirySource = lead.getDmsSourceOfEnquiry().getName();
        this.enquiryDate = lead.getDateOfEnquiry();
        this.commitmentDeliveryPreferredDate = lead.getCommitmentDeliveryPreferredDate();
        this.model = lead.getModel();
        this.enquirySegment = lead.getEnquirySegment();
        this.leadStage = lead.getLeadStage();
        this.enquiryCategory = lead.getEnquiryCategory();
        this.createdBy = lead.getCreatedBy();
        this.salesConsultant = lead.getSalesConsultant();
        this.email = lead.getEmail();
        this.leadStatus = lead.getLeadStatus();
        this.referencenumber= lead.getReferencenumber();
    }

}
