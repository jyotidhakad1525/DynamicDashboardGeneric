package com.automate.df.entity.sales.master;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "enquiry_segment")
public class EnquirySegment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "segment_type", nullable = false)
    private String segmentType;
}
