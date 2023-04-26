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
@Table(name = "dms_received_booking_amount")
@NamedQuery(name = "DmsBookingAmountReceived.findAll", query = "SELECT d FROM DmsBookingAmountReceived d")
public class DmsBookingAmountReceived implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "payment_name")
    private String paymentName;

    @Column(name = "amount")
    private Double amount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_datetime")
    private Date createdDateTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_datetime")
    private Date modifiedDateTime;

    @ManyToOne
    @JoinColumn(name = "lead_id")
    private DmsLead dmsLead;
}

