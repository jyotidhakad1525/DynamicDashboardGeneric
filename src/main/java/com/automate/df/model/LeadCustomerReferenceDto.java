package com.automate.df.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LeadCustomerReferenceDto {

    private int id;
    private int leadId;
    private int branchid;
    private Date createdDatetime;
    private String leadstage;
    private int orgid;
    private String referencenumber;

}
