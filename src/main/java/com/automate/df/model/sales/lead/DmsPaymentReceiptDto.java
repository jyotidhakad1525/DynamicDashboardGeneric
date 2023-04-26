package com.automate.df.model.sales.lead;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;


@Setter
@Getter
public class DmsPaymentReceiptDto {

    private int id;

    private String bankName;

    private int chequeNo;

    private Date date;

    private String ddNo;

    private String paymentMode;

    private BigInteger transferFromMobile;

    private BigInteger transferToMobile;

    private String typeUpi;

    private String utrNo;

    private int leadId;

    private int bookingId;

}
