package com.automate.df.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class DmsExchangeBuyerDto {

    private int id;
    private String buyerType;
    private String regNo;
    private String brand;
    private String model;
    private String varient;
    private String color;
    private String fuelType;
    private String transmission;
    private String yearofManufacture;
    private String kiloMeters;

    private boolean hypothicationRequirement;
    private String hypothication;
    private String hypothicationBranch;
    private BigDecimal expectedPrice;
    private String registrationDate;
    private String registrationValidityDate;

    private String insuranceAvailable;
    private Date insuranceExpiryDate;
    private String insuranceCompanyName;
    private boolean insuranceDocumentAvailable;
    private String insuranceType;
    private String insuranceFromDate;
    private String insuranceToDate;

    private String insuranceDocumentPath;
    private String insuranceDocumentKey;
    private String regDocumentPath;
    private String regDocumentKey;
    private String otherExpensessHypothication;


    //not using
    private BigDecimal challansAmount;
    private String ccClearanceExpenses;
    private String nocClearanceExpenses;
    private String otherExpenses;
    private String laonAmountDue;
    private String loancompletion;
    private BigDecimal offeredPrice;
    private BigDecimal finalPrice;
    private String noofOwners;
    private String challansPending;

}
