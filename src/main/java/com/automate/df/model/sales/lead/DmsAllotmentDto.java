package com.automate.df.model.sales.lead;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class DmsAllotmentDto {

    private int id;
    private String location;
    private String vinno;
    private String engineNo;
    private String model;
    private String varient;
    private String color;
    private String fuel;
    private String chassisNo;
    private String keyNo;
    private Date createDateTime;
    private Date modifiedDateTime;
    private int leadId;
    private int bookingId;
}
