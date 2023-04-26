package com.automate.df.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VehicleImage implements Serializable {

    private static final long serialVersionUID = 9023377754106515829L;


    private String vehicleImageId;


    private String vehicleId;


    private String varient_id;

  
    private String color;

 
    private String color_top_code;


    private String color_body_code;


    private String priority;


    private String url;

    private String dualColor;
    private Boolean is_dual_color;
    
	String sno;
	String parentIdentifier;
}
