package com.automate.df.entity.sales.master;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;


@Setter
@Getter
@Entity
@Table(name = "dms_source_of_enquiries")
@NamedQuery(name = "DmsSourceOfEnquiry.findAll", query = "SELECT d FROM DmsSourceOfEnquiry d")
public class DmsSourceOfEnquiry implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

    private String name;

    private String value;
    
    @Column(name = "created_at")
    private Timestamp createdAt;
    
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "created_by")
    private String createdBy;
    
    @Column(name = "updated_by")
    private String updatedBy;
    
    @Column(name = "org_id")
    private String orgId;
    
}