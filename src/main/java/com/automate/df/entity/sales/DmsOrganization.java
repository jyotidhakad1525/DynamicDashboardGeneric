package com.automate.df.entity.sales;

import com.automate.df.entity.oh.DmsAddress;
import com.automate.df.entity.oh.DmsBranch;
import com.automate.df.entity.oh.DmsDesignation;
import com.automate.df.entity.sales.employee.*;
import com.automate.df.entity.sales.lead.DmsAccount;
import com.automate.df.entity.sales.lead.DmsAttachment;
import com.automate.df.entity.sales.lead.DmsContact;
import com.automate.df.entity.sales.workflow.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "dms_organization")
@NamedQuery(name = "DmsOrganization.findAll", query = "SELECT d FROM DmsOrganization d")
public class DmsOrganization implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "org_id")
    private int orgId;

    private String brand;

    @Column(name = "domain_name")
    private String domainName;

    private String email;

    @Column(name = "logo_big_url")
    private String logoBigUrl;

    @Column(name = "logo_small_url")
    private String logoSmallUrl;

    private BigInteger mobile;

    @Column(name = "cin")
    private String cinNumber;

    private String name;

    private BigInteger phone;

    private String status;

    private String website;

    private String url;


    @Column(name = "s3_name")
    private String s3Name;

    @Column(name = "document_url")
    private String documentUrl;


    // bi-directional many-to-one association to DmsAccount
    @OneToMany(mappedBy = "dmsOrganization")
    private List<DmsAccount> dmsAccounts;

    // bi-directional many-to-one association to DmsAction
    @OneToMany(mappedBy = "dmsOrganization")
    private List<DmsAction> dmsActions;

    // bi-directional many-to-one association to DmsAttachment
    @OneToMany(mappedBy = "dmsOrganization")
    private List<DmsAttachment> dmsAttachments;

    // bi-directional many-to-one association to DmsBranch
    @OneToMany(mappedBy = "dmsOrganization")
    private List<DmsBranch> dmsBranches;

    // bi-directional many-to-one association to DmsContact
    @OneToMany(mappedBy = "dmsOrganization")
    private List<DmsContact> dmsContacts;

    // bi-directional many-to-one association to DmsDepartment
    @OneToMany(mappedBy = "dmsOrganization")
    private List<DmsDepartment> dmsDepartments;

    // bi-directional many-to-one association to DmsDesignation
    @OneToMany(mappedBy = "dmsOrganization")
    private List<DmsDesignation> dmsDesignations;

    // bi-directional many-to-one association to DmsEmployee
    @OneToMany(mappedBy = "dmsOrganization")
    private List<DMSEmployee> DMSEmployees;

    // bi-directional many-to-one association to DmsEmployeeRoleMapping
    @OneToMany(mappedBy = "dmsOrganization")
    private List<DmsEmployeeRoleMapping> dmsEmployeeRoleMappings;

    // bi-directional many-to-one association to DmsEmployeeStatusMd
    @OneToMany(mappedBy = "dmsOrganization")
    private List<DmsEmployeeStatusMd> dmsEmployeeStatusMds;

    // bi-directional many-to-one association to DmsAddress
    @ManyToOne
    @JoinColumn(name = "address")
    private DmsAddress dmsAddress;

    // bi-directional many-to-one association to DmsRole
    @OneToMany(mappedBy = "dmsOrganization")
    private List<DmsRole> dmsRoles;

    // bi-directional many-to-one association to DmsWorkflowActivityDef
    @OneToMany(mappedBy = "dmsOrganization")
    private List<DMSActivityDef> DMSActivityDefs;

    // bi-directional many-to-one association to DmsWorkflowDef
    @OneToMany(mappedBy = "dmsOrganization")
    private List<DMSWorkflowDef> DMSWorkflowDefs;

    // bi-directional many-to-one association to DmsWorkflowProcessDef
    @OneToMany(mappedBy = "dmsOrganization")
    private List<DMSProcessDef> DMSProcessDefs;

    // bi-directional many-to-one association to DmsWorkflowTaskDef
    @OneToMany(mappedBy = "dmsOrganization")
    private List<DMSTaskDef> DMSTaskDefs;


    //bi-directional many-to-one association to DmsTaskApprover
    @OneToMany(mappedBy = "dmsOrganization")
    private List<DmsTaskApprover> dmsTaskApprovers;

    public DmsAccount addDmsAccount(DmsAccount dmsAccount) {
        getDmsAccounts().add(dmsAccount);
        dmsAccount.setDmsOrganization(this);

        return dmsAccount;
    }

    public DmsAccount removeDmsAccount(DmsAccount dmsAccount) {
        getDmsAccounts().remove(dmsAccount);
        dmsAccount.setDmsOrganization(null);

        return dmsAccount;
    }

    public DmsAction addDmsAction(DmsAction dmsAction) {
        getDmsActions().add(dmsAction);
        dmsAction.setDmsOrganization(this);

        return dmsAction;
    }

    public DmsAction removeDmsAction(DmsAction dmsAction) {
        getDmsActions().remove(dmsAction);
        dmsAction.setDmsOrganization(null);

        return dmsAction;
    }

    public DmsAttachment addDmsAttachment(DmsAttachment dmsAttachment) {
        getDmsAttachments().add(dmsAttachment);
        dmsAttachment.setDmsOrganization(this);

        return dmsAttachment;
    }

    public DmsAttachment removeDmsAttachment(DmsAttachment dmsAttachment) {
        getDmsAttachments().remove(dmsAttachment);
        dmsAttachment.setDmsOrganization(null);

        return dmsAttachment;
    }

    public DmsBranch addDmsBranch(DmsBranch dmsBranch) {
        getDmsBranches().add(dmsBranch);
        dmsBranch.setDmsOrganization(this);

        return dmsBranch;
    }

    public DmsBranch removeDmsBranch(DmsBranch dmsBranch) {
        getDmsBranches().remove(dmsBranch);
        dmsBranch.setDmsOrganization(null);

        return dmsBranch;
    }

    public DmsContact addDmsContact(DmsContact dmsContact) {
        getDmsContacts().add(dmsContact);
        dmsContact.setDmsOrganization(this);

        return dmsContact;
    }

    public DmsContact removeDmsContact(DmsContact dmsContact) {
        getDmsContacts().remove(dmsContact);
        dmsContact.setDmsOrganization(null);

        return dmsContact;
    }

    public DmsDepartment addDmsDepartment(DmsDepartment dmsDepartment) {
        getDmsDepartments().add(dmsDepartment);
        dmsDepartment.setDmsOrganization(this);

        return dmsDepartment;
    }

    public DmsDepartment removeDmsDepartment(DmsDepartment dmsDepartment) {
        getDmsDepartments().remove(dmsDepartment);
        dmsDepartment.setDmsOrganization(null);

        return dmsDepartment;
    }

    public DmsDesignation addDmsDesignation(DmsDesignation dmsDesignation) {
        getDmsDesignations().add(dmsDesignation);
        dmsDesignation.setDmsOrganization(this);

        return dmsDesignation;
    }

    public DmsDesignation removeDmsDesignation(DmsDesignation dmsDesignation) {
        getDmsDesignations().remove(dmsDesignation);
        dmsDesignation.setDmsOrganization(null);

        return dmsDesignation;
    }

    public DMSEmployee addDmsEmployee(DMSEmployee dmsEmployee) {
        getDMSEmployees().add(dmsEmployee);
        dmsEmployee.setDmsOrganization(this);

        return dmsEmployee;
    }

    public DMSEmployee removeDmsEmployee(DMSEmployee dmsEmployee) {
        getDMSEmployees().remove(dmsEmployee);
        dmsEmployee.setDmsOrganization(null);

        return dmsEmployee;
    }

    public DmsEmployeeRoleMapping addDmsEmployeeRoleMapping(DmsEmployeeRoleMapping dmsEmployeeRoleMapping) {
        getDmsEmployeeRoleMappings().add(dmsEmployeeRoleMapping);
        dmsEmployeeRoleMapping.setDmsOrganization(this);

        return dmsEmployeeRoleMapping;
    }

    public DmsEmployeeRoleMapping removeDmsEmployeeRoleMapping(DmsEmployeeRoleMapping dmsEmployeeRoleMapping) {
        getDmsEmployeeRoleMappings().remove(dmsEmployeeRoleMapping);
        dmsEmployeeRoleMapping.setDmsOrganization(null);

        return dmsEmployeeRoleMapping;
    }

    public DmsEmployeeStatusMd addDmsEmployeeStatusMd(DmsEmployeeStatusMd dmsEmployeeStatusMd) {
        getDmsEmployeeStatusMds().add(dmsEmployeeStatusMd);
        dmsEmployeeStatusMd.setDmsOrganization(this);

        return dmsEmployeeStatusMd;
    }

    public DmsEmployeeStatusMd removeDmsEmployeeStatusMd(DmsEmployeeStatusMd dmsEmployeeStatusMd) {
        getDmsEmployeeStatusMds().remove(dmsEmployeeStatusMd);
        dmsEmployeeStatusMd.setDmsOrganization(null);

        return dmsEmployeeStatusMd;
    }

    public DmsRole addDmsRole(DmsRole dmsRole) {
        getDmsRoles().add(dmsRole);
        dmsRole.setDmsOrganization(this);

        return dmsRole;
    }

    public DmsRole removeDmsRole(DmsRole dmsRole) {
        getDmsRoles().remove(dmsRole);
        dmsRole.setDmsOrganization(null);

        return dmsRole;
    }

    public DMSActivityDef addDmsWorkflowActivityDef(DMSActivityDef DMSActivityDef) {
        getDMSActivityDefs().add(DMSActivityDef);
        DMSActivityDef.setDmsOrganization(this);

        return DMSActivityDef;
    }

    public DMSActivityDef removeDmsWorkflowActivityDef(DMSActivityDef DMSActivityDef) {
        getDMSActivityDefs().remove(DMSActivityDef);
        DMSActivityDef.setDmsOrganization(null);

        return DMSActivityDef;
    }

    public DMSWorkflowDef addDmsWorkflowDef(DMSWorkflowDef dmsWorkflowDef) {
        getDMSWorkflowDefs().add(dmsWorkflowDef);
        dmsWorkflowDef.setDmsOrganization(this);

        return dmsWorkflowDef;
    }

    public DMSWorkflowDef removeDmsWorkflowDef(DMSWorkflowDef dmsWorkflowDef) {
        getDMSWorkflowDefs().remove(dmsWorkflowDef);
        dmsWorkflowDef.setDmsOrganization(null);

        return dmsWorkflowDef;
    }

    public DMSProcessDef addDmsWorkflowProcessDef(DMSProcessDef dmsProcessDef) {
        getDMSProcessDefs().add(dmsProcessDef);
        dmsProcessDef.setDmsOrganization(this);

        return dmsProcessDef;
    }

    public DMSProcessDef removeDmsWorkflowProcessDef(DMSProcessDef dmsProcessDef) {
        getDMSProcessDefs().remove(dmsProcessDef);
        dmsProcessDef.setDmsOrganization(null);

        return dmsProcessDef;
    }

    public DMSTaskDef addDmsWorkflowTaskDef(DMSTaskDef DMSTaskDef) {
        getDMSTaskDefs().add(DMSTaskDef);
        DMSTaskDef.setDmsOrganization(this);

        return DMSTaskDef;
    }

    public DMSTaskDef removeDmsWorkflowTaskDef(DMSTaskDef DMSTaskDef) {
        getDMSTaskDefs().remove(DMSTaskDef);
        DMSTaskDef.setDmsOrganization(null);

        return DMSTaskDef;
    }

}