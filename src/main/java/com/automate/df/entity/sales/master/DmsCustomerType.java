package com.automate.df.entity.sales.master;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;


@Setter
@Getter
@Entity
@Table(name = "dms_customer_type")
@NamedQuery(name = "DmsCustomerType.findAll", query = "SELECT d FROM DmsCustomerType d")
public class DmsCustomerType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "customer_type")
    private String customerType;
    
    @Column(name = "enquiry_segment")
    private String enquirySegment;
    
    @Column(name = "is_active")
    private Boolean isActive;
    
    @Column(name = "created_at")
    private Timestamp createdAt;
    
    @Column(name = "updated_at")
    private Timestamp updatedAt;
 
    @Column(name = "org_id")
    private String orgId; 
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "created_by")
    private String createdBy;
    
    @Column(name = "updated_by")
    private String updatedBy;
    
    
    
}