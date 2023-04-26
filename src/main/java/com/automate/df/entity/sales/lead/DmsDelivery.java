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
@Table(name = "dms_delivery")
@NamedQuery(name = "dms_delivery.findAll", query = "SELECT d FROM DmsDelivery d")
public class DmsDelivery implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "challan_no")
    private String challanNo;

    @Temporal(TemporalType.DATE)
    @Column(name = "challan_date")
    private Date challanDate;

    @Column(name = "hypothicated_to")
    private String hypothicatedTo;

    @Column(name = "insurance_company")
    private String insuranceCompany;

    @Column(name = "insurance_policy_no")
    private String insurancePolicyNo;

    @Temporal(TemporalType.DATE)
    @Column(name = "insurance_date")
    private Date insuranceDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "insurence_exp_date")
    private Date insurenceExpDate;


    @Column(name = "rc_no")
    private String rcNo;

    @Column(name = "etd_warranty_no")
    private String etdWarrantyNo;

    @Column(name = "fasttag_no")
    private String fastTagNo;

    @Column(name = "original_key")
    private Boolean originalKey;

    @Column(name = "duplicate_key")
    private Boolean duplicateKey;

    @Column(name = "user_manual")
    private Boolean userManual;

    @Column(name = "service_booklet")
    private Boolean serviceBooklet;

    @Column(name = "tool_kit")
    private Boolean toolKit;

    @Column(name = "jack")
    private Boolean jack;

    @Column(name = "jack_rod")
    private Boolean jackRod;

    @Column(name = "spare_wheel")
    private Boolean spareWheel;

    @Column(name = "insurence_copy")
    private Boolean insurenceCopy;

    @Column(name = "tr_copy")
    private Boolean trCopy;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_datetime")
    private Date createdDateTime;

    //bi-directional many-to-one association to DmsLead
    @ManyToOne
    @JoinColumn(name = "lead_id")
    private DmsLead dmsLead;

    //bi-directional many-to-one association to DmsBooking
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private com.automate.df.entity.sales.lead.DmsBooking dmsBooking;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private com.automate.df.entity.sales.lead.DmsInvoice dmsInvoice;

}


