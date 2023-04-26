package com.automate.df.entity.sales.lead;

import com.automate.df.entity.dashboard.DmsLead;
import com.automate.df.enums.StateType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "dms_invoice")
@NamedQuery(name = "dms_invoice.findAll", query = "SELECT d FROM DmsInvoice d")
public class DmsInvoice implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "invoice_type")
    private String invoiceType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "invoice_date")
    private Date invoiceDate;

    @Column(name = "financer_name")
    private String financerName;

    @Column(name = "branch")
    private String branch;

    @Column(name = "corporate_code")
    private String corporateCode;

    @Column(name = "corporate_name")
    private String corporateName;

    @Enumerated(EnumType.STRING)
    @Column(name = "state_type")
    private StateType stateType;

    @Column(name = "gst")
    private String gst;

    @Column(name = "gst_rate")
    private Integer gst_rate;

    @Column(name = "cess_percentage")
    private Integer cessPercentage;

    @Column(name = "total_tax")
    private Double totalTax;

    @Column(name = "engine_cc")
    private Integer engineCc;

    @Column(name = "basic_price")
    private Double basicPrice;

    @Column(name = "cess_amount")
    private Double cessAmount;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_datetime")
    private Date createdDatetime;

    @ManyToOne
    @JoinColumn(name = "lead_id")
    private DmsLead dmsLead;

    //bi-directional many-to-one association to DmsBooking
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private DmsBooking dmsBooking;
}
