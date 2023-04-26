package com.automate.df.entity.dashboard;

import com.automate.df.entity.SubSource;
import com.automate.df.entity.oh.DmsAddress;
import com.automate.df.entity.oh.DmsBranch;
import com.automate.df.entity.sales.allocation.DmsEmployeeAllocation;
import com.automate.df.entity.sales.employee.DmsExchangeBuyer;
import com.automate.df.entity.sales.lead.DmsAttachment;
import com.automate.df.entity.sales.master.DmsSourceOfEnquiry;
import com.automate.df.entity.sales.DmsOrganization;
import com.automate.df.enums.CustomerCategoryType;
import com.automate.df.enums.DocumentType;
import com.automate.df.enums.MaritalStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "dms_lead")
public class DmsLead implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "activity_id")
	private String activityId;

	private String aging;

	private String allocated;

	@Column(name = "buyer_type")
	private String buyerType;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "sub_source")
	private String subSource;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createddatetime;

	@Column(name = "crm_universal_id")
	private String crmUniversalId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_of_enquiry")
	private Date dateOfEnquiry;

	@Temporal(TemporalType.DATE)
	@Column(name = "dms_expected_delivery_date")
	private Date dmsExpectedDeliveryDate;

	private String email;

	@Column(name = "enquiry_category")
	private String enquiryCategory;

	@Column(name = "enquiry_segment")
	private String enquirySegment;

	@Column(name = "event_code")
	private String eventCode;

	@Column(name = "finance_required")
	private Boolean financeRequired;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "lead_stage")
	private String leadStage;

	@Column(name = "lead_status")
	private String leadStatus;

	@Column(name = "lead_tag")
	private String leadTag;

	@Column(name = "lead_type")
	private String leadType;

	@Column(name = "mode_of_payment")
	private String modeOfPayment;

	private String model;

	private Timestamp modifieddatetime;

	private String phone;

	private String remarks;

	@Column(name = "sales_consultant")
	private String salesConsultant;

	@Enumerated(EnumType.STRING)
	@Column(name = "marital_status")
	private MaritalStatus maritalStatus;

	@Temporal(TemporalType.DATE)
	@Column(name = "commitment_delivery_preferred_date")
	private Date commitmentDeliveryPreferredDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "commitment_delivery_tentative_date")
	private Date commitmentDeliveryTentativeDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "document_type")
	private DocumentType documenttype;

	@Column(name = "document_no")
	private String documentNo;

	@Column(name = "gst_number")
	private String gstNumber;

	@Enumerated(EnumType.STRING)
	@Column(name = "cust_category_type")
	private CustomerCategoryType customerCategoryType;

	@Column(name = "other_vehicle_rc_no")
	private String otherVehicleRcNo;

	@Column(name = "delivery_occasion")
	private String deliveryOccasion;

	@Column(name = "delivery_time")
	private String deliveryTime;

	@Column(name = "other_vehicle_type")
	private String otherVehicleType;

	@Column(name = "occasion")
	private String occasion;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "occasion_date")
	private Date occasionDate;

	@ManyToOne
	@JoinColumn(name = "organization_id")
	private DmsOrganization dmsOrganization;

	//bi-directional many-to-one association to DmsBranch
	@ManyToOne
	@JoinColumn(name = "branch_id")
	private DmsBranch dmsBranch;

	@ManyToOne
	@JoinColumn(name = "source_of_enquiry")
	private DmsSourceOfEnquiry dmsSourceOfEnquiry;

	@ManyToOne
	@JoinColumn(name = "sub_source_id")
	private SubSource subSourceOfEnquiry;

	//bi-directional many-to-one association to DmsAddress
	@OneToMany(mappedBy = "dmsLead")
	private List<DmsAddress> dmsAddresses;

	//bi-directional many-to-one association to DmsAttachment
	@OneToMany(mappedBy = "dmsLead")
	private List<DmsAttachment> dmsAttachments;

	//bi-directional many-to-one association to DmsEmployeeAllocation
	@OneToMany(mappedBy = "dmsLead")
	private List<DmsEmployeeAllocation> dmsEmployeeAllocations;

	//bi-directional many-to-one association to DmsExchangeBuyer
	@OneToMany(mappedBy = "dmsLead")
	private List<DmsExchangeBuyer> dmsExchangeBuyers;

	//bi-directional many-to-one association to DmsFinanceDetail
	@OneToMany(mappedBy = "dmsLead")
	private List<com.automate.df.entity.sales.lead.DmsFinanceDetail> dmsFinanceDetails;

	//bi-directional many-to-one association to DmsAccount
	@ManyToOne
	@JoinColumn(name = "dms_account_id")
	private com.automate.df.entity.sales.lead.DmsAccount dmsAccount;

	//bi-directional many-to-one association to DmsContact
	@ManyToOne
	@JoinColumn(name = "dms_contact_id")
	private com.automate.df.entity.sales.lead.DmsContact dmsContact;

	//bi-directional many-to-one association to DmsLeadProduct
	@OneToMany(mappedBy = "dmsLead")
	private List<com.automate.df.entity.sales.lead.DmsLeadProduct> dmsLeadProducts;

	//bi-directional many-to-one association to DmsLeadScoreCard
	@OneToMany(mappedBy = "dmsLead")
	private List<com.automate.df.entity.sales.lead.DmsLeadScoreCard> dmsLeadScoreCards;

	@OneToMany(mappedBy = "dmsLead")
	private List<com.automate.df.entity.sales.lead.DmsBooking> dmsBookings;
	@Column(name = "referencenumber")
	private String referencenumber;

	public DmsAddress addDmsAddress(DmsAddress dmsAddress) {
		getDmsAddresses().add(dmsAddress);
		dmsAddress.setDmsLead(this);

		return dmsAddress;
	}

	public DmsAddress removeDmsAddress(DmsAddress dmsAddress) {
		getDmsAddresses().remove(dmsAddress);
		dmsAddress.setDmsLead(null);

		return dmsAddress;
	}

	public DmsAttachment addDmsAttachment(DmsAttachment dmsAttachment) {
		getDmsAttachments().add(dmsAttachment);
		dmsAttachment.setDmsLead(this);

		return dmsAttachment;
	}

	public DmsAttachment removeDmsAttachment(DmsAttachment dmsAttachment) {
		getDmsAttachments().remove(dmsAttachment);
		dmsAttachment.setDmsLead(null);

		return dmsAttachment;
	}

	public DmsEmployeeAllocation addDmsEmployeeAllocation(DmsEmployeeAllocation dmsEmployeeAllocation) {
		getDmsEmployeeAllocations().add(dmsEmployeeAllocation);
		dmsEmployeeAllocation.setDmsLead(this);

		return dmsEmployeeAllocation;
	}

	public DmsEmployeeAllocation removeDmsEmployeeAllocation(DmsEmployeeAllocation dmsEmployeeAllocation) {
		getDmsEmployeeAllocations().remove(dmsEmployeeAllocation);
		dmsEmployeeAllocation.setDmsLead(null);

		return dmsEmployeeAllocation;
	}

	public DmsExchangeBuyer addDmsExchangeBuyer(DmsExchangeBuyer dmsExchangeBuyer) {
		getDmsExchangeBuyers().add(dmsExchangeBuyer);
		dmsExchangeBuyer.setDmsLead(this);

		return dmsExchangeBuyer;
	}

	public DmsExchangeBuyer removeDmsExchangeBuyer(DmsExchangeBuyer dmsExchangeBuyer) {
		getDmsExchangeBuyers().remove(dmsExchangeBuyer);
		dmsExchangeBuyer.setDmsLead(null);

		return dmsExchangeBuyer;
	}

	public com.automate.df.entity.sales.lead.DmsFinanceDetail addDmsFinanceDetail(com.automate.df.entity.sales.lead.DmsFinanceDetail dmsFinanceDetail) {
		getDmsFinanceDetails().add(dmsFinanceDetail);
		dmsFinanceDetail.setDmsLead(this);

		return dmsFinanceDetail;
	}

	public com.automate.df.entity.sales.lead.DmsFinanceDetail removeDmsFinanceDetail(com.automate.df.entity.sales.lead.DmsFinanceDetail dmsFinanceDetail) {
		getDmsFinanceDetails().remove(dmsFinanceDetail);
		dmsFinanceDetail.setDmsLead(null);

		return dmsFinanceDetail;
	}

	public com.automate.df.entity.sales.lead.DmsLeadProduct addDmsLeadProduct(com.automate.df.entity.sales.lead.DmsLeadProduct dmsLeadProduct) {
		getDmsLeadProducts().add(dmsLeadProduct);
		dmsLeadProduct.setDmsLead(this);

		return dmsLeadProduct;
	}

	public com.automate.df.entity.sales.lead.DmsLeadProduct removeDmsLeadProduct(com.automate.df.entity.sales.lead.DmsLeadProduct dmsLeadProduct) {
		getDmsLeadProducts().remove(dmsLeadProduct);
		dmsLeadProduct.setDmsLead(null);

		return dmsLeadProduct;
	}

	public com.automate.df.entity.sales.lead.DmsLeadScoreCard addDmsLeadScoreCard(com.automate.df.entity.sales.lead.DmsLeadScoreCard dmsLeadScoreCard) {
		getDmsLeadScoreCards().add(dmsLeadScoreCard);
		dmsLeadScoreCard.setDmsLead(this);

		return dmsLeadScoreCard;
	}

	public com.automate.df.entity.sales.lead.DmsLeadScoreCard removeDmsLeadScoreCard(com.automate.df.entity.sales.lead.DmsLeadScoreCard dmsLeadScoreCard) {
		getDmsLeadScoreCards().remove(dmsLeadScoreCard);
		dmsLeadScoreCard.setDmsLead(null);

		return dmsLeadScoreCard;
	}

	public com.automate.df.entity.sales.lead.DmsBooking addDmsBooking(com.automate.df.entity.sales.lead.DmsBooking dmsBooking) {
		getDmsBookings().add(dmsBooking);
		dmsBooking.setDmsLead(this);

		return dmsBooking;
	}

	public com.automate.df.entity.sales.lead.DmsBooking removeDmsBooking(com.automate.df.entity.sales.lead.DmsBooking dmsBooking) {
		getDmsBookings().remove(dmsBooking);
		dmsBooking.setDmsLead(null);

		return dmsBooking;
	}


}