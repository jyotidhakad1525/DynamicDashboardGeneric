package com.automate.df.entity.sales.lead;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;


@Setter
@Getter
@Entity
@Table(name = "dms_received_booking_amount")
public class DmsReceivedBookingAmount {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "lead_id")
    private int leadId;

    @Column(name = "amount")
    private BigInteger amount;

    @Column(name = "payment_name")
    private String paymentName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_datetime")
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_datetime")
    private Date modifiedDate;

}
