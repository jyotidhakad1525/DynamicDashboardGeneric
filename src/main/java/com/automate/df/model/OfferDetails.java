package com.automate.df.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OfferDetails {


    private String id;

    private String model;
    
    private String variant;
    
    private String vehicleId;
    
    private String variantId;
    
    private String organizationId;

    private String offerName;
 
    private String amount;

    private String shotDescription;

    private String longDescription;

    private String offerType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String end_date;


    private String status;


    private String smallIconUrl;

    private String imageUrl;

    private String htmlUrl;


    private String isCheckboxAllowed;



    private String createdBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") 
    private String createdDate;
    private String modifiedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String modifiedDate;
    private String confirmationMassage;




}
