package com.automate.df.entity.sales.master;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;


@Setter
@Getter
@Entity
@Table(name = "dms_enquiry_segment")
@NamedQuery(name = "DmsEnquirySegment.findAll", query = "SELECT d FROM DmsEnquirySegment d")
public class DmsEnquirySegment implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "segment_type")
    private String segmentType;

    @Column(name = "status")
    private String status;
    
    @Column(name = "created_at")
    private Timestamp createdAt;
    
    @Column(name = "updated_at")
    private Timestamp updatedAt;
 
    @Column(name = "org_id")
    private String orgId; 
    
    @Column(name = "created_by")
    private String createdBy;
    
    @Column(name = "updated_by")
    private String updatedBy;
}