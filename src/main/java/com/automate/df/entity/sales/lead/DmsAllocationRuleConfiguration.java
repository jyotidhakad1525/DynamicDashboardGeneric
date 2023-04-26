package com.automate.df.entity.sales.lead;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Setter
@Getter
@Entity
@Table(name = "dms_allocation_rule_configuration")
@NamedQuery(name = "DmsAllocationRuleConfiguration.findAll", query = "SELECT d FROM DmsAllocationRuleConfiguration d")
public class DmsAllocationRuleConfiguration implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String fuel;

    private String model;

    private String variant;
    

    @Column(name = "model_status")
    private String modelStatus;

    @Column(name = "Status")
    private String status;
    
    @Column(name = "created_at")
    private String createdAt;
    
    @Column(name = "updated_at")
    private String updatedAt;
    
    @Column(name = "created_by")
    private String createdBy;
    
    @Column(name = "updated_by")
    private String updatedBy;
    
    @Column(name = "approvel_id")
    private String approvelId;
    
    @Column(name = "approvel_required")
    private String approvelRequired;
    
    @Column(name = "booking_id")
    private String bookingId;
    
    @Column(name = "booking_amount")
    private double bookingAmount;
    
    @Column(name = "de_allocation_indays")
    private String deAllocationIndays;
    
    @Column(name = "org_id")
    private String orgId; 

    @Column(name = "accessories_name")
    private String accessoriesName;

    @Column(name = "allotement_status")
    private Boolean allotementStatus;


}
