package com.automate.df.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;



import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VehicleVarient implements Serializable {

    private static final long serialVersionUID = -4198136128926450791L;
 
    private String id;

    private String name;

    private String vehicleId;
 
//    @Enumerated(EnumType.STRING)
    private String fuelType;

//    @Enumerated(EnumType.STRING)
    private String transmission_type;

    private String mileage;
  
//    @Enumerated(EnumType.STRING)
    private String status;

    private String enginecc;

    private String bhp;
    
	String sno;
	String parentIdentifier;
    
    
    private Set<VehicleImage> vehicleImages;

    enum FuelType {
        Petrol,
        Diesel,
        Electric,
        CNG,
        LPG_Petrol
    }

    enum TransmissionType {
        Automatic,
        Manual
    }

}
