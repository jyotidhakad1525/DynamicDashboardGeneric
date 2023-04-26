package com.automate.df.entity.sales.employee;

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
@Table(name = "dms_employee_role_mapping")
@NamedQuery(name = "DmsEmployeeRoleMapping.findAll", query = "SELECT d FROM DmsEmployeeRoleMapping d")
public class DmsEmployeeRoleMapping implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "created_by")
    private String createdBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "updated_by")
    private String updatedBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "updated_date")
    private Date updatedDate;

    @Column(name = "leads")
    private Integer leads;

    @Column(name = "max_target")
    private Integer maxTarget;

    @Column(name = "dmslead_count")
    private Integer dmsLeadCount;

    //bi-directional many-to-one association to DmsEmployee
    @ManyToOne
    @JoinColumn(name = "emp_id")
    private com.automate.df.entity.sales.employee.DMSEmployee dmsEmployee;

    //bi-directional many-to-one association to DmsRole
    @ManyToOne
    @JoinColumn(name = "role_id")
    private com.automate.df.entity.sales.employee.DmsRole dmsRole;

    //bi-directional many-to-one association to DmsOrganization
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private DmsOrganization dmsOrganization;

    //bi-directional many-to-one association to DmsBranch
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private DmsBranch dmsBranch;

    //bi-directional many-to-one association to DmsDepartment
    @ManyToOne
    @JoinColumn(name = "department_id")
    private com.automate.df.entity.sales.employee.DmsDepartment dmsDepartment;

}