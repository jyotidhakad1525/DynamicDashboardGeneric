package com.automate.df.model;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VehicleOnRoadPrice {


    private String id;

    private String organization_id;

    private String model;
    
    private String variant;
    
    private String vehicle_id;

    private String varient_id;

    private String ex_showroom_price;

    private String registration_charges;

    private String handling_charges;
    

    private String ex_showroom_price_csd;

    private String hypothecation_price;

    private String tcs_percentage;

    private String tcs_amount;

    private String essential_kit;


    private String fast_tag;


    private String vehicle_road_tax;
    
    private String cess_percentage;


    private Set<RoadTax> roadtax;


    private Set<ExtendedWaranty> extended_waranty;


    private Set<InsuranceVarientMapping> insurance_vareint_mapping;


    private Set<InsuranceAddOn> insuranceAddOn;
    
	String sno;
	String parentIdentifier;
}
