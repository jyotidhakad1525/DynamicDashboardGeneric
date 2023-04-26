package com.automate.df.entity.sales.employee;

import com.automate.df.entity.oh.DmsBranch;
import com.automate.df.entity.oh.DmsDesignation;
import com.automate.df.entity.sales.DmsOrganization;
import com.automate.df.entity.sales.workflow.DMSTaskCategory;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "dms_department")
@NamedQuery(name = "DmsDepartment.findAll", query = "SELECT d FROM DmsDepartment d")
public class DmsDepartment implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dms_department_id")
    private int dmsDepartmentId;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "created_by")
    private String createdBy;

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "department_code")
    private String departmentCode;

    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "hrms_department_id")
    private String hrmsDepartmentId;

    @Column(name = "is_active")
    private Boolean isActive;

    private String status;

    @Column(name = "updated_by")
    private String updatedBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "updated_time")
    private Date updatedTime;

    //bi-directional many-to-one association to DmsOrganization
    @ManyToOne
    @JoinColumn(name = "org_id")
    private DmsOrganization dmsOrganization;

    //bi-directional many-to-one association to DmsBranch
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private DmsBranch dmsBranch;

    //bi-directional many-to-one association to DmsDesignation
    @OneToMany(mappedBy = "dmsDepartment")
    private List<DmsDesignation> dmsDesignations;

    //bi-directional many-to-one association to DmsEmployee
    @OneToMany(mappedBy = "dmsDepartment")
    private List<DMSEmployee> DMSEmployees;

    //bi-directional many-to-one association to DmsEmployeeRoleMapping
    @OneToMany(mappedBy = "dmsDepartment")
    private List<DmsEmployeeRoleMapping> dmsEmployeeRoleMappings;

    //bi-directional many-to-one association to DmsRole
    @OneToMany(mappedBy = "dmsDepartment")
    private List<DmsRole> dmsRoles;

    //bi-directional many-to-one association to DmsTaskCategory
    @OneToMany(mappedBy = "dmsDepartment")
    private List<DMSTaskCategory> dmsTaskCategories;

    @Column(name = "location")
    private String location;

    public DmsDesignation addDmsDesignation(DmsDesignation dmsDesignation) {
        getDmsDesignations().add(dmsDesignation);
        dmsDesignation.setDmsDepartment(this);

        return dmsDesignation;
    }

    public DmsDesignation removeDmsDesignation(DmsDesignation dmsDesignation) {
        getDmsDesignations().remove(dmsDesignation);
        dmsDesignation.setDmsDepartment(null);

        return dmsDesignation;
    }

    public DMSEmployee addDmsEmployee(DMSEmployee dmsEmployee) {
        getDMSEmployees().add(dmsEmployee);
        dmsEmployee.setDmsDepartment(this);

        return dmsEmployee;
    }

    public DMSEmployee removeDmsEmployee(DMSEmployee dmsEmployee) {
        getDMSEmployees().remove(dmsEmployee);
        dmsEmployee.setDmsDepartment(null);

        return dmsEmployee;
    }

    public DmsEmployeeRoleMapping addDmsEmployeeRoleMapping(DmsEmployeeRoleMapping dmsEmployeeRoleMapping) {
        getDmsEmployeeRoleMappings().add(dmsEmployeeRoleMapping);
        dmsEmployeeRoleMapping.setDmsDepartment(this);

        return dmsEmployeeRoleMapping;
    }

    public DmsEmployeeRoleMapping removeDmsEmployeeRoleMapping(DmsEmployeeRoleMapping dmsEmployeeRoleMapping) {
        getDmsEmployeeRoleMappings().remove(dmsEmployeeRoleMapping);
        dmsEmployeeRoleMapping.setDmsDepartment(null);

        return dmsEmployeeRoleMapping;
    }

    public DmsRole addDmsRole(DmsRole dmsRole) {
        getDmsRoles().add(dmsRole);
        dmsRole.setDmsDepartment(this);

        return dmsRole;
    }

    public DmsRole removeDmsRole(DmsRole dmsRole) {
        getDmsRoles().remove(dmsRole);
        dmsRole.setDmsDepartment(null);

        return dmsRole;
    }

    public DMSTaskCategory addDmsTaskCategory(DMSTaskCategory dmsTaskCategory) {
        getDmsTaskCategories().add(dmsTaskCategory);
        dmsTaskCategory.setDmsDepartment(this);

        return dmsTaskCategory;
    }

    public DMSTaskCategory removeDmsTaskCategory(DMSTaskCategory dmsTaskCategory) {
        getDmsTaskCategories().remove(dmsTaskCategory);
        dmsTaskCategory.setDmsDepartment(null);

        return dmsTaskCategory;
    }

}