package com.automate.df.model.sales.lead;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
public class DmsDeliveryDto {

    private int id;
    private int leadId;
    private int bookingId;
    private int invoiceId;
    private String challanNo;
    private Date challanDate;
    private String hypothicatedTo;
    private String insuranceCompany;
    private String insurancePolicyNo;
    private Date insuranceDate;
    private Date insurenceExpDate;
    private Boolean userManual;
    private String rcNo;
    private String etdWarrantyNo;
    private String fastTagNo;
    private Boolean originalKey;
    private Boolean duplicateKey;
    private Boolean serviceBooklet;
    private Boolean toolKit;
    private Boolean jack;
    private Boolean jackRod;
    private Boolean spareWheel;
    private Boolean insurenceCopy;
    private Boolean trCopy;
    private Date createdDatetime;

}