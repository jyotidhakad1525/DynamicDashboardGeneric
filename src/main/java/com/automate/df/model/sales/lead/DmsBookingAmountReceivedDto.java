package com.automate.df.model.sales.lead;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class DmsBookingAmountReceivedDto {

    private int id;
    private String paymentName;
    private Double amount;
    private Date createdDateTime;
    private Date modifiedDateTime;
    private int leadId;

}
