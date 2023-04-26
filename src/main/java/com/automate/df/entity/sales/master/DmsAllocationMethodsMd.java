package com.automate.df.entity.sales.master;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Setter
@Getter
@Entity
@Table(name = "dms_allocation_methods_md")
@NamedQuery(name = "DmsAllocationMethodsMd.findAll", query = "SELECT d FROM DmsAllocationMethodsMd d")
public class DmsAllocationMethodsMd implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "created_by")
    private String createdBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_time")
    private Date createdTime;

    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

    private String name;

    private String status;

    @Column(name = "updated_by")
    private String updatedBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "updated_time")
    private Date updatedTime;

    public DmsAllocationMethodsMd() {
    }

}