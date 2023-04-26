package com.automate.df.model;

import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InsuranceAddOn {


    private String id;

    private String model;
    
    private String variant;
    
    private String organization_id;

    private String vehicle_id;

    private String addOnPrice;
    
    @Convert(converter = JpaJsonDocumentsListConverter.class)
    private List<HashMap<String, Object>> add_on_price;

    private String varient_id;
    
	String sno;
	String parentIdentifier;
}
