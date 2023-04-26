package com.automate.df.entity.sales.master;

import com.automate.df.entity.sales.employee.DMSEmployee;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "dms_marital_status_md")
@NamedQuery(name = "DmsMaritalStatusMd.findAll", query = "SELECT d FROM DmsMaritalStatusMd d")
public class DmsMaritalStatusMd implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dms_marital_status_id")
    private int dmsMaritalStatusId;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "branch_id")
    private int branchId;

    @Column(name = "created_by")
    private String createdBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "hrms_marital_status_id")
    private String hrmsMaritalStatusId;

    @Column(name = "is_active")
    private Boolean isActive;

    private String name;

    @Column(name = "org_id")
    private int orgId;

    private String status;

    @Column(name = "updated_by")
    private String updatedBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "updated_time")
    private Date updatedTime;

    @OneToMany(mappedBy = "dmsMaritalStatusMd")
    private List<DMSEmployee> DMSEmployees;

    public DMSEmployee addDmsEmployee(DMSEmployee dmsEmployee) {
        getDMSEmployees().add(dmsEmployee);
        dmsEmployee.setDmsMaritalStatusMd(this);

        return dmsEmployee;
    }

    public DMSEmployee removeDmsEmployee(DMSEmployee dmsEmployee) {
        getDMSEmployees().remove(dmsEmployee);
        dmsEmployee.setDmsMaritalStatusMd(null);

        return dmsEmployee;
    }

}