package com.automate.df.entity.sales.employee;

import com.automate.df.entity.oh.DmsBranch;
import com.automate.df.entity.sales.DmsOrganization;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "dms_role")
@NamedQuery(name = "DmsRole.findAll", query = "SELECT d FROM DmsRole d")
public class DmsRole implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleId;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "creation_date")
    private Timestamp creationDate;

    private String description;

    @Column(name = "hrms_role_id")
    private String hrmsRoleId;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "role_name")
    private String roleName;

    //bi-directional many-to-one association to DmsEmployee
    @OneToMany(mappedBy = "dmsRole")
    private List<com.automate.df.entity.sales.employee.DMSEmployee> DMSEmployees;

    //bi-directional many-to-one association to DmsEmployeeRoleMapping
    @OneToMany(mappedBy = "dmsRole")
    private List<DmsEmployeeRoleMapping> dmsEmployeeRoleMappings;

    //bi-directional many-to-one association to DmsOrganization
    @ManyToOne
    @JoinColumn(name = "org_id")
    private DmsOrganization dmsOrganization;

    //bi-directional many-to-one association to DmsBranch
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private DmsBranch dmsBranch;

    //bi-directional many-to-one association to DmsDepartment
    @ManyToOne
    @JoinColumn(name = "department_id")
    private com.automate.df.entity.sales.employee.DmsDepartment dmsDepartment;

    public com.automate.df.entity.sales.employee.DMSEmployee addDmsEmployee(com.automate.df.entity.sales.employee.DMSEmployee dmsEmployee) {
        getDMSEmployees().add(dmsEmployee);
        dmsEmployee.setDmsRole(this);

        return dmsEmployee;
    }

    public com.automate.df.entity.sales.employee.DMSEmployee removeDmsEmployee(com.automate.df.entity.sales.employee.DMSEmployee dmsEmployee) {
        getDMSEmployees().remove(dmsEmployee);
        dmsEmployee.setDmsRole(null);

        return dmsEmployee;
    }

    public DmsEmployeeRoleMapping addDmsEmployeeRoleMapping(DmsEmployeeRoleMapping dmsEmployeeRoleMapping) {
        getDmsEmployeeRoleMappings().add(dmsEmployeeRoleMapping);
        dmsEmployeeRoleMapping.setDmsRole(this);

        return dmsEmployeeRoleMapping;
    }

    public DmsEmployeeRoleMapping removeDmsEmployeeRoleMapping(DmsEmployeeRoleMapping dmsEmployeeRoleMapping) {
        getDmsEmployeeRoleMappings().remove(dmsEmployeeRoleMapping);
        dmsEmployeeRoleMapping.setDmsRole(null);

        return dmsEmployeeRoleMapping;
    }


}