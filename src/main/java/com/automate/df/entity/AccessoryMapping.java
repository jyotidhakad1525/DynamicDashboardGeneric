package com.automate.df.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;



@Setter
@Getter
@Entity
@Table(name = "kit_accessories_mapping")
public class AccessoryMapping {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "kit_name")
    private String kitName;
    @Column(name = "accessories_list")
    private String accessoriesList;
    @Column(name = "origanistion_id")
    private String organisationId;
    @Column(name = "vehicle_id")
    private String vehicleId;
    @Column(name = "cost")
    private String cost;
    
}