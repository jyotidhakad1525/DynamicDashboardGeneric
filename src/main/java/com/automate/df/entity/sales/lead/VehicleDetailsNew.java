package com.automate.df.entity.sales.lead;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


@Setter
@Getter
@Entity
@Table(name = "vehicle_management.vehicle_details_new")
public class VehicleDetailsNew {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "organization_id")
    private String organizationId;
    private String type;

    private String model;
    @Column(name = "image_url")
    private String imageUrl;


    private Integer created_by;
    private Integer modified_by;

    private Timestamp created_date;
    private String status;
    private Timestamp modified_date;
    private BigDecimal booking_amount;
    private String waiting_period;
    private String description;
    private String type_category;
    private String price_range;


}
