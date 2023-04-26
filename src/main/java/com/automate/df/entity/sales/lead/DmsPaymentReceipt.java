package com.automate.df.entity.sales.lead;

import com.automate.df.entity.dashboard.DmsLead;
import com.automate.df.enums.PaymentMode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;


@Setter
@Getter
@Entity
@Table(name = "dms_payment_receipt")
@NamedQuery(name = "DmsPaymentReceipt.findAll", query = "SELECT d FROM DmsPaymentReceipt d")
public class DmsPaymentReceipt implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "cheque_no")
    private int chequeNo;

    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "dd_no")
    private String ddNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode")
    private PaymentMode paymentMode;

    @Column(name = "transfer_from_mobile")
    private BigInteger transferFromMobile;

    @Column(name = "transfer_to_mobile")
    private BigInteger transferToMobile;

    @Column(name = "type_upi")
    private String typeUpi;

    @Column(name = "utr_no")
    private String utrNo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_date")
    private Date modifiedDate;

    //bi-directional many-to-one association to DmsLead
    @ManyToOne
    @JoinColumn(name = "lead_id")
    private DmsLead dmsLead;

    //bi-directional many-to-one association to DmsBooking
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private DmsBooking dmsBooking;

}