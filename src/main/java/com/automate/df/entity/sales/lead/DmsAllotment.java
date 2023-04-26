package com.automate.df.entity.sales.lead;


import com.automate.df.entity.dashboard.DmsLead;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "dms_allotment")
@NamedQuery(name = "dms_allotment.findAll", query = "SELECT d FROM DmsAllotment d")
public class DmsAllotment implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "location")
    private String location;

    @Column(name = "vinno")
    private String vinno;

    @Column(name = "engineno")
    private String engineNo;

    @Column(name = "model")
    private String model;

    @Column(name = "varient")
    private String varient;

    @Column(name = "color")
    private String color;

    @Column(name = "fuel")
    private String fuel;

    @Column(name = "chassisno")
    private String chassisNo;

    @Column(name = "keyno")
    private String keyNo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_datetime")
    private Date createDateTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_datetime")
    private Date modifiedDateTime;

    //bi-directional many-to-one association to DmsLead
    @ManyToOne
    @JoinColumn(name = "lead_id")
    private DmsLead dmsLead;

    //bi-directional many-to-one association to DmsBooking
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private DmsBooking dmsBooking;


}

