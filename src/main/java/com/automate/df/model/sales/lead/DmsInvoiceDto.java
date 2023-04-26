package com.automate.df.model.sales.lead;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class DmsInvoiceDto {

    private int id;
    private String invoiceType;
    private Date invoiceDate;
    private String financerName;
    private String branch;
    private String corporateCode;
    private String corporateName;
    private String stateType;
    private String gst;
    private Integer gst_rate;
    private Integer cessPercentage;
    private Double totalTax;
    private Integer engineCc;
    private Double basicPrice;
    private Double cessAmount;
    private Double totalAmount;
    private Date createdDatetime;
    private int leadId;

    private int bookingId;

}
