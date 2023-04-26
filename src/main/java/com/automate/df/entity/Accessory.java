package com.automate.df.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
@Table(name = "accessories")
public class Accessory {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "vehicle_id")
    private Integer vehicleId;
    @Column(name = "origanistion_id")
    private String organizationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private com.automate.df.enums.Category category;

    @Column(name = "item")
    private String item;
    @Column(name = "part_name")
    private String partName;
    @Column(name = "cost")
    private BigDecimal cost;
    @Column(name = "created_by")
    private Integer createdBy;
    @Column(name = "part_no")
    private String partNo;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "created_date")
    private String createdDate;
    @Column(name = "modified_by")
    private Integer modifiedBy;
    @Column(name = "modified_date")
    private String modifiedDate;

}