package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LeadCustRefReq {
    public int branchid;
    public String leadstage;
    public int orgid;
}
