package com.automate.df.model;


import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Accessory {

 
    private String id;

    private String vehicleId;
    
    private String model;
    
    private String variant;

    private String origanistionId;


    private String Category;


    private String item;

    private String partName;

    private String cost;

    private String createdBy;

    private String partNo;

    private String imageUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private String createdDate;

    private String modifiedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private String modifiedDate;

}
