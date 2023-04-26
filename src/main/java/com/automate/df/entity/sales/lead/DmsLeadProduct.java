package com.automate.df.entity.sales.lead;

import com.automate.df.entity.dashboard.DmsLead;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Setter
@Getter
@Entity
@Table(name = "dms_lead_product")
@NamedQuery(name = "DmsLeadProduct.findAll", query = "SELECT d FROM DmsLeadProduct d")
public class DmsLeadProduct implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String color;

    private String fuel;

    private String model;

    @Column(name = "transimmision_type")
    private String transimmisionType;

    private String variant;

    @ManyToOne
    @JoinColumn(name = "lead_id")
    private DmsLead dmsLead;

}