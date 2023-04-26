package com.automate.df.entity.oh;

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
@Table(name = "dms_designation")
@NamedQuery(name = "DmsDesignation.findAll", query = "SELECT d FROM DmsDesignation d")
public class DmsDesignation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dms_designation_id")
	private int dmsDesignationId;

	@Column(name = "approved_by")
	private String approvedBy;

	@Column(name = "created_by")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "created_time")
	private Date createdTime;

	@Column(name = "department_code")
	private String departmentCode;

	@Column(name = "department_name")
	private String departmentName;

	@Column(name = "hrms_designation_id")
	private String hrmsDesignationId;

	@Column(name = "is_active")
	private Boolean isActive;

	private String status;

	@Column(name = "updated_by")
	private String updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "updated_time")
	private Date updatedTime;

	@Column(name = "designation_name")
	private String designationName;

	// bi-directional many-to-one association to DmsOrganization
	@ManyToOne
	@JoinColumn(name = "org_id")
	private DmsOrganization dmsOrganization;

	// bi-directional many-to-one association to DmsBranch
	@ManyToOne
	@JoinColumn(name = "branch_id")
	private DmsBranch dmsBranch;

	@Column(name="level")
	private Integer level;

	// bi-directional many-to-one association to DmsDepartment
	@ManyToOne
	@JoinColumn(name = "department_id")
	private com.automate.df.entity.sales.employee.DmsDepartment dmsDepartment;

	// bi-directional many-to-one association to DmsEmployee
	@OneToMany(mappedBy = "dmsDesignation")
	private List<com.automate.df.entity.sales.employee.DMSEmployee> DMSEmployees;

	public com.automate.df.entity.sales.employee.DMSEmployee addDmsEmployee(com.automate.df.entity.sales.employee.DMSEmployee dmsEmployee) {
		getDMSEmployees().add(dmsEmployee);
		dmsEmployee.setDmsDesignation(this);

		return dmsEmployee;
	}

	public com.automate.df.entity.sales.employee.DMSEmployee removeDmsEmployee(com.automate.df.entity.sales.employee.DMSEmployee dmsEmployee) {
		getDMSEmployees().remove(dmsEmployee);
		dmsEmployee.setDmsDesignation(null);

		return dmsEmployee;
	}

}