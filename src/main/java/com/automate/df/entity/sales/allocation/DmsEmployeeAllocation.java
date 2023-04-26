package com.automate.df.entity.sales.allocation;

import com.automate.df.entity.dashboard.DmsLead;
import com.automate.df.entity.oh.DmsBranch;
import com.automate.df.entity.sales.DmsOrganization;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Setter
@Getter
@Entity
@Table(name = "dms_employee_allocation")
@NamedQuery(name = "DmsEmployeeAllocation.findAll", query = "SELECT d FROM DmsEmployeeAllocation d")
public class DmsEmployeeAllocation implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_by")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createddatetime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_of_allocation")
    private Date dateOfAllocation;

    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "employee_name")
    private String employeeName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_date")
    private Date modifiedDate;

    private String status;

    //bi-directional many-to-one association to DmsLead
    @ManyToOne
    @JoinColumn(name = "lead_id")
    private DmsLead dmsLead;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private DmsOrganization dmsOrganization;

    //bi-directional many-to-one association to DmsBranch
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private DmsBranch dmsBranch;

}