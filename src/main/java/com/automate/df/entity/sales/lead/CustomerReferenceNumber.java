package com.automate.df.entity.sales.lead;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Setter
@Getter
@Entity
@Table(name = "customer_reference_number")
@NamedQuery(name = "CustomerReferenceNumber.findAll", query = "SELECT c FROM CustomerReferenceNumber c")
public class CustomerReferenceNumber implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int branchid;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_datetime")
    private Date createdDatetime;

    private String leadstage;

    private int orgid;

    private String referencenumber;

}