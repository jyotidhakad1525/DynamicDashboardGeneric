package com.automate.df.model;

import java.util.Date;
import java.util.List;

import com.automate.df.model.sales.lead.DmsAddressDto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DmsLeadDto {
	
	/*public int id;
		public int branchId;
	    public String createdBy;
	    public String enquirySegment;
	    public String firstName;
	    public String lastName;
	    public String leadStage;
	    public String model;
	    public int organizationId;
	    public String phone;
	    public int sourceOfEnquiry;
	    public String eventCode;
	    public String email;
	    public String referencenumber;
	    public List<DmsAddress> dmsAddresses;*/
	
	 private int id;
	    private String activityId;
	    private String aging;
	    private String firstName;
	    private String phone;
	    private String lastName;
	    private String email;
	    private String allocated;
	    private int branchId;
	    private String buyerType;
	    private String createdBy;
	    private Date createddatetime;
	    private String crmUniversalId;
	    private Date dateOfEnquiry;
	    private Date dmsExpectedDeliveryDate;
	    private Date bookingDate;
	    private String leadStatus;
	    private String enquiryCategory;
	    private String enquirySegment;
	    private String eventCode;
	    private Boolean financeRequired;
	    private String leadStage;
	    private String leadTag;
	    private String leadType;
	    private String modeOfPayment;
	    private String model;
	    private int organizationId;
	    private String remarks;
	    private String salesConsultant;
	    private Integer sourceOfEnquiry;
	    private Integer subSourceOfEnquiry;
	    private int dmsaccountid;
	    private int dmscontactid;
	    private String enquirySource;
	    private String subSource;
	    private String maritalStatus;
	    private Date commitmentDeliveryPreferredDate;
	    private Date commitmentDeliveryTentativeDate;
	    private String documentType;
	    private String documentNo;
	    private String gstNumber;
	    private String customerCategoryType;
	    private String otherVehicleRcNo;
	    private String deliveryOccasion;
	    private String deliveryTime;
	    private String otherVehicleType;
	    private String occasion;
	    private Date occasionDate;
	    private String referencenumber;

	    private List<DmsAddress> dmsAddresses;
	    

	    private List<DmsLeadProductDto> dmsLeadProducts;
	    private List<DmsFinanceDetailsDto> dmsfinancedetails;
	    private List<DmsLeadScoreCardDto> dmsLeadScoreCards;
	    private List<DmsAttachmentDto> dmsAttachments;
	    private List<DmsExchangeBuyerDto> dmsExchagedetails;
	    private DmsBookingDto dmsBooking;
	   private List<DmsAccessoriesDto> dmsAccessories;
	    
	    
	
}
