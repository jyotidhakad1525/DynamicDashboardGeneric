package com.automate.df.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class DmsBookingDto {

    private int id;
    private Double bookingAmount;
    private String paymentAt;
    private String modeOfPayment;
    private int leadId;
    private Boolean otherVehicle;
    private String addOnCovers;
    private String warranty;
    private Boolean npsScheme;
    private Boolean exchangeOffer;
    private String corporateName;
    private Boolean corporateOffer;
    private Boolean ruralOffer;
    private Double promotionalOffers;
    private Double cashDiscount;
    private Double focAccessories;
    private Double addOffer1;
    private Double addOffer2;
    private Date createdDate;
    private Date modifiedDate;
    private Double specialScheme;
    private String reasonVehicleAllocation;
    private String deliveryLocation;
    private String receivedBookingAmount;
    private Double totalPaid;
    private String vinNo;
    private String engineNo;
    private int model;
    private int varient;
    private String fuel;
    private int color;
    private String chassisNo;
    private String keyNo;
    private String insuranceType;

}
