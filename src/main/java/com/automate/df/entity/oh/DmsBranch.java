package com.automate.df.entity.oh;

import com.automate.df.entity.sales.DmsAction;
import com.automate.df.entity.sales.DmsOrganization;
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
@Table(name = "dms_branch")
@NamedQuery(name = "DmsBranch.findAll", query = "SELECT d FROM DmsBranch d")
public class DmsBranch implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "branch_id")
	private int branchId;

	@Column(name = "store_id")
	private String storeId;

	@Column(name = "branch_type")
	private String branchType;

	private String email;

	private BigInteger mobile;

	@Column(name = "cinNumber")
	private String cinNumber;

	private String name;

	private BigInteger phone;

	private String status;

	private String website;

	@Column(name = "s3_name")
	private String s3Name;

	@Column(name = "document_url")
	private String documentUrl;


	// bi-directional many-to-one association to DmsAccount
	@OneToMany(mappedBy = "dmsBranch")
	private List<DmsAccount> dmsAccounts;

	// bi-directional many-to-one association to DmsAction
	@OneToMany(mappedBy = "dmsBranch")
	private List<DmsAction> dmsActions;

	// bi-directional many-to-one association to DmsAttachment
	@OneToMany(mappedBy = "dmsBranch")
	private List<DmsAttachment> dmsAttachments;

	// bi-directional many-to-one association to DmsOrganization
	@ManyToOne
	@JoinColumn(name = "organization_id")
	private DmsOrganization dmsOrganization;

	// bi-directional many-to-one association to DmsAddress
	@ManyToOne
	@JoinColumn(name = "address")
	private DmsAddress dmsAddress;

	// bi-directional many-to-one association to DmsContact
	@OneToMany(mappedBy = "dmsBranch")
	private List<DmsContact> dmsContacts;

	// bi-directional many-to-one association to DmsDepartment
	@OneToMany(mappedBy = "dmsBranch")
	private List<DmsDepartment> dmsDepartments;

	// bi-directional many-to-one association to DmsDesignation
	@OneToMany(mappedBy = "dmsBranch")
	private List<DmsDesignation> dmsDesignations;

	// bi-directional many-to-one association to DmsEmployee
	@OneToMany(mappedBy = "dmsBranch", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<DMSEmployee> _DMSEmployees;

	// bi-directional many-to-one association to DmsEmployeeRoleMapping
	@OneToMany(mappedBy = "dmsBranch")
	private List<DmsEmployeeRoleMapping> dmsEmployeeRoleMappings;

	// bi-directional many-to-one association to DmsEmployeeStatusMd
	@OneToMany(mappedBy = "dmsBranch")
	private List<DmsEmployeeStatusMd> dmsEmployeeStatusMds;

	// bi-directional many-to-one association to DmsRole
	@OneToMany(mappedBy = "dmsBranch")
	private List<DmsRole> dmsRoles;

	// bi-directional many-to-one association to DmsWorkflowActivityDef
	@OneToMany(mappedBy = "dmsBranch", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<DMSActivityDef> _DMSActivityDefs;

	// bi-directional many-to-one association to DmsWorkflowDef
	@OneToMany(mappedBy = "dmsBranch")
	private List<DMSWorkflowDef> dMSWorkflowDefs;

	// bi-directional many-to-one association to DmsWorkflowProcessDef
	@OneToMany(mappedBy = "dmsBranch", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<DMSProcessDef> _DMSProcessDefs;

	// bi-directional many-to-one association to DmsWorkflowTaskDef
	@OneToMany(mappedBy = "dmsBranch", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<DMSTaskDef> _DMSTaskDefs;

	//bi-directional many-to-one association to DmsTaskApprover
	@OneToMany(mappedBy = "dmsBranch")
	private List<DmsTaskApprover> dmsTaskApprovers;

	@Column(name = "org_map_id")
	private int orgMapId;

	@Column(name="active")
	private String active;

	@Column(name="adress")
	private Integer adress;

	public DmsAction addDmsAction(DmsAction dmsAction) {
		getDmsActions().add(dmsAction);
		dmsAction.setDmsBranch(this);

		return dmsAction;
	}

	public DmsAction removeDmsAction(DmsAction dmsAction) {
		getDmsActions().remove(dmsAction);
		dmsAction.setDmsBranch(null);

		return dmsAction;
	}

	public DmsAttachment addDmsAttachment(DmsAttachment dmsAttachment) {
		getDmsAttachments().add(dmsAttachment);
		dmsAttachment.setDmsBranch(this);

		return dmsAttachment;
	}

	public DmsAttachment removeDmsAttachment(DmsAttachment dmsAttachment) {
		getDmsAttachments().remove(dmsAttachment);
		dmsAttachment.setDmsBranch(null);

		return dmsAttachment;
	}

	public DmsContact addDmsContact(DmsContact dmsContact) {
		getDmsContacts().add(dmsContact);
		dmsContact.setDmsBranch(this);

		return dmsContact;
	}

	public DmsContact removeDmsContact(DmsContact dmsContact) {
		getDmsContacts().remove(dmsContact);
		dmsContact.setDmsBranch(null);

		return dmsContact;
	}

	public DmsDepartment addDmsDepartment(DmsDepartment dmsDepartment) {
		getDmsDepartments().add(dmsDepartment);
		dmsDepartment.setDmsBranch(this);

		return dmsDepartment;
	}

	public DmsDepartment removeDmsDepartment(DmsDepartment dmsDepartment) {
		getDmsDepartments().remove(dmsDepartment);
		dmsDepartment.setDmsBranch(null);

		return dmsDepartment;
	}

	public DmsDesignation addDmsDesignation(DmsDesignation dmsDesignation) {
		getDmsDesignations().add(dmsDesignation);
		dmsDesignation.setDmsBranch(this);

		return dmsDesignation;
	}

	public DmsDesignation removeDmsDesignation(DmsDesignation dmsDesignation) {
		getDmsDesignations().remove(dmsDesignation);
		dmsDesignation.setDmsBranch(null);

		return dmsDesignation;
	}

	public DMSEmployee addDmsEmployee(DMSEmployee dmsEmployee) {
		get_DMSEmployees().add(dmsEmployee);
		dmsEmployee.setDmsBranch(this);

		return dmsEmployee;
	}

	public DMSEmployee removeDmsEmployee(DMSEmployee dmsEmployee) {
		get_DMSEmployees().remove(dmsEmployee);
		dmsEmployee.setDmsBranch(null);

		return dmsEmployee;
	}

	public DmsEmployeeRoleMapping addDmsEmployeeRoleMapping(DmsEmployeeRoleMapping dmsEmployeeRoleMapping) {
		getDmsEmployeeRoleMappings().add(dmsEmployeeRoleMapping);
		dmsEmployeeRoleMapping.setDmsBranch(this);

		return dmsEmployeeRoleMapping;
	}

	public DmsEmployeeRoleMapping removeDmsEmployeeRoleMapping(DmsEmployeeRoleMapping dmsEmployeeRoleMapping) {
		getDmsEmployeeRoleMappings().remove(dmsEmployeeRoleMapping);
		dmsEmployeeRoleMapping.setDmsBranch(null);

		return dmsEmployeeRoleMapping;
	}

	public DmsEmployeeStatusMd addDmsEmployeeStatusMd(DmsEmployeeStatusMd dmsEmployeeStatusMd) {
		getDmsEmployeeStatusMds().add(dmsEmployeeStatusMd);
		dmsEmployeeStatusMd.setDmsBranch(this);

		return dmsEmployeeStatusMd;
	}

	public DmsEmployeeStatusMd removeDmsEmployeeStatusMd(DmsEmployeeStatusMd dmsEmployeeStatusMd) {
		getDmsEmployeeStatusMds().remove(dmsEmployeeStatusMd);
		dmsEmployeeStatusMd.setDmsBranch(null);

		return dmsEmployeeStatusMd;
	}

	public DmsRole addDmsRole(DmsRole dmsRole) {
		getDmsRoles().add(dmsRole);
		dmsRole.setDmsBranch(this);

		return dmsRole;
	}

	public DmsRole removeDmsRole(DmsRole dmsRole) {
		getDmsRoles().remove(dmsRole);
		dmsRole.setDmsBranch(null);

		return dmsRole;
	}

	public DMSActivityDef addDmsWorkflowActivityDef(DMSActivityDef DMSActivityDef) {
		get_DMSActivityDefs().add(DMSActivityDef);
		DMSActivityDef.setDmsBranch(this);

		return DMSActivityDef;
	}

	public DMSActivityDef removeDmsWorkflowActivityDef(DMSActivityDef DMSActivityDef) {
		get_DMSActivityDefs().remove(DMSActivityDef);
		DMSActivityDef.setDmsBranch(null);

		return DMSActivityDef;
	}

	public DMSWorkflowDef addDmsWorkflowDef(DMSWorkflowDef dmsWorkflowDef) {
		getDMSWorkflowDefs().add(dmsWorkflowDef);
		dmsWorkflowDef.setDmsBranch(this);

		return dmsWorkflowDef;
	}

	public DMSWorkflowDef removeDmsWorkflowDef(DMSWorkflowDef dmsWorkflowDef) {
		getDMSWorkflowDefs().remove(dmsWorkflowDef);
		dmsWorkflowDef.setDmsBranch(null);

		return dmsWorkflowDef;
	}

	public DMSProcessDef addDmsWorkflowProcessDef(DMSProcessDef dmsProcessDef) {
		get_DMSProcessDefs().add(dmsProcessDef);
		dmsProcessDef.setDmsBranch(this);

		return dmsProcessDef;
	}

	public DMSProcessDef removeDmsWorkflowProcessDef(DMSProcessDef dmsProcessDef) {
		get_DMSProcessDefs().remove(dmsProcessDef);
		dmsProcessDef.setDmsBranch(null);

		return dmsProcessDef;
	}

	public DMSTaskDef addDmsWorkflowTaskDef(DMSTaskDef DMSTaskDef) {
		get_DMSTaskDefs().add(DMSTaskDef);
		DMSTaskDef.setDmsBranch(this);

		return DMSTaskDef;
	}

	public DMSTaskDef removeDmsWorkflowTaskDef(DMSTaskDef DMSTaskDef) {
		get_DMSTaskDefs().remove(DMSTaskDef);
		DMSTaskDef.setDmsBranch(null);

		return DMSTaskDef;
	}

}