package com.automate.df.model;

import java.io.Serializable;
import java.util.Set;



import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VehicleDetails implements Serializable {
    private static final long serialVersionUID = 4895908114629386216L;

    
	String sno;
	String parentIdentifier;
    private String vehicleId;
   
    private String organizationId;

//    @Enumerated(EnumType.STRING)
    private String type;

    private String model;

    private String imageUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private String createdDate;

    private String createdBy;

    private String modifiedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private String modifiedDate;

//    @Enumerated(EnumType.STRING)
    private String status;

    private String booking_amount;

    private String waiting_period;

    private String description;
//    @Enumerated(EnumType.STRING)
    private String typeCategory;

    private String priceRange;
    
    private Set<VehicleVarient> varients;

    public enum Type {
        Car,
        MotorCycle,
        Auto,
        Truck,
        Tractor
    }

    public enum TypeCategory {
        Passenger,
        Cargo,
        SPV_Customization,
        Hatchback,
        Sedan,
        Mini_Car,
        SUV
    }

}
