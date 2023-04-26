package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LeadCustomerReference {
    public int id;
    public int branchid;
    public Object createdDatetime;
    public String leadstage;
    public int orgid;
    public String referencenumber;
}
