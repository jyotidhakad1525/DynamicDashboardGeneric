package com.automate.df.entity.sales.employee;

import com.automate.df.entity.oh.DmsBranch;
import com.automate.df.entity.sales.DmsOrganization;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "dms_employee_status_md")
@NamedQuery(name = "DmsEmployeeStatusMd.findAll", query = "SELECT d FROM DmsEmployeeStatusMd d")
public class DmsEmployeeStatusMd implements Serializable {
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

    //bi-directional many-to-one association to DmsEmployee
    @OneToMany(mappedBy = "dmsEmployeeStatusMd")
    private List<com.automate.df.entity.sales.employee.DMSEmployee> DMSEmployees;

    //bi-directional many-to-one association to DmsOrganization
    @ManyToOne
    @JoinColumn(name = "org_id")
    private DmsOrganization dmsOrganization;

    //bi-directional many-to-one association to DmsBranch
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private DmsBranch dmsBranch;

    public com.automate.df.entity.sales.employee.DMSEmployee addDmsEmployee(com.automate.df.entity.sales.employee.DMSEmployee dmsEmployee) {
        getDMSEmployees().add(dmsEmployee);
        dmsEmployee.setDmsEmployeeStatusMd(this);

        return dmsEmployee;
    }

    public com.automate.df.entity.sales.employee.DMSEmployee removeDmsEmployee(com.automate.df.entity.sales.employee.DMSEmployee dmsEmployee) {
        getDMSEmployees().remove(dmsEmployee);
        dmsEmployee.setDmsEmployeeStatusMd(null);

        return dmsEmployee;
    }

}