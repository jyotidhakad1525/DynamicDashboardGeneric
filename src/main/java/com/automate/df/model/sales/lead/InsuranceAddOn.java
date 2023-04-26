package com.automate.df.model.sales.lead;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class InsuranceAddOn implements Serializable {

    private Double insuranceAmount;
    private String insuranceAddonName;


}
