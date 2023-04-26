package com.automate.df.model;

import java.util.HashMap;
import java.util.List;

import javax.persistence.Convert;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExtendedWaranty {



    private String id;

    private String model;
    
    private String variant;
    
    private String organization_id;

    private String vehicle_id;

    private String varient_id;

    private String extendedWarranty;
//    
//    @Convert(converter = JpaJsonDocumentsListConverter.class)
//    private List<HashMap<String, Object>> warranty;
   private String warranty;
    
	String sno;
	String parentIdentifier;
	
}
