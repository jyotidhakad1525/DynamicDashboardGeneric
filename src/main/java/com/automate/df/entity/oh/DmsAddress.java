package com.automate.df.entity.oh;

import com.automate.df.entity.dashboard.DmsLead;
import com.automate.df.entity.sales.DmsOrganization;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "dms_address")
@NamedQuery(name = "DmsAddress.findAll", query = "SELECT d FROM DmsAddress d")
public class DmsAddress implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "address_type")
	private String addressType;

	private String city;

	private String country;

	private String district;

	@Column(name = "house_no")
	private String houseNo;

	private String latitude;

	private String longitude;

	private String pincode;

	private String state;

	private String street;

	private String village;

	@Column(name = "preferred_billing_address")
	private String preferredBillingAddress;

	@Column(name = "is_rural")
	private Boolean isRural;

	@Column(name = "is_urban")
	private Boolean isUrban;

	//bi-directional many-to-one association to DmsLead
	@ManyToOne
	@JoinColumn(name = "dms_lead_id")
	private DmsLead dmsLead;

	//bi-directional many-to-one association to DmsBranch
	@OneToMany(mappedBy = "dmsAddress")
	private List<DmsBranch> dmsBranches;

	//bi-directional many-to-one association to DmsOrganization
	@OneToMany(mappedBy = "dmsAddress")
	private List<DmsOrganization> dmsOrganizations;

	@Column(name="active")
	private String active;

	public Boolean getRural() {
		return isRural;
	}

	public void setRural(Boolean rural) {
		isRural = rural;
	}

	public Boolean getUrban() {
		return isUrban;
	}

	public void setUrban(Boolean urban) {
		isUrban = urban;
	}

	public DmsBranch addDmsBranch(DmsBranch dmsBranch) {
		getDmsBranches().add(dmsBranch);
		dmsBranch.setDmsAddress(this);

		return dmsBranch;
	}

	public DmsBranch removeDmsBranch(DmsBranch dmsBranch) {
		getDmsBranches().remove(dmsBranch);
		dmsBranch.setDmsAddress(null);

		return dmsBranch;
	}

	public DmsOrganization addDmsOrganization(DmsOrganization dmsOrganization) {
		getDmsOrganizations().add(dmsOrganization);
		dmsOrganization.setDmsAddress(this);

		return dmsOrganization;
	}

	public DmsOrganization removeDmsOrganization(DmsOrganization dmsOrganization) {
		getDmsOrganizations().remove(dmsOrganization);
		dmsOrganization.setDmsAddress(null);

		return dmsOrganization;
	}

}