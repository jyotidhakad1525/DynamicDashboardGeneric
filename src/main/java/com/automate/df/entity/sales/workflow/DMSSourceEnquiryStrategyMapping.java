package com.automate.df.entity.sales.workflow;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "dms_source_enquiry_strategy_mapping")
public class DMSSourceEnquiryStrategyMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "branch_id")
    private int branch_id;

    @Column(name = "org_id")
    private int org_id;

    @Column(name = "source_enquiry_id")
    private int source_enquiry_id;

    @Column(name = "allocation_id")
    private int allocation_id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_datetime")
    private Date created_datetime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_datetime")
    private Date modified_datetime;

}
