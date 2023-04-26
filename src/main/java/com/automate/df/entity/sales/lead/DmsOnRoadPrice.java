package com.automate.df.entity.sales.lead;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.automate.df.entity.dashboard.DmsLead;
import com.automate.df.model.sales.lead.InsuranceAddOn;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Entity
@Table(name = "dms_onroad_price")
@NamedQuery(name = "dms_onroad_price.findAll", query = "SELECT d FROM DmsOnRoadPrice d")
public class DmsOnRoadPrice implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "ex_showroom_price")
    private Double exShowroomPrice;
    @Column(name = "life_tax")
    private Double lifeTax;
    @Column(name = "registration_charges")
    private Double registrationCharges;
    @Column(name = "insurance_type")
    private String insuranceType;
    @Column(name = "insurance_amount")
    private Double insuranceAmount;
    @Column(name = "insurance_add_on")
    private Double insuranceAddOn;
    @Column(name = "insurance_addon_name")
    private String insuranceAddonName;
    @Column(name = "warranty_name")
    private String warrantyName;
    @Column(name = "warranty_amount")
    private Double warrantyAmount;
    @Column(name = "handling_charges")
    private Double handlingCharges;
    @Column(name = "essential_kit")
    private Double essentialKit;
    @Column(name = "tcs")
    private Double tcs;
    @Column(name = "dms_onroad_pricecol")
    private String dmsOnroadPriceCol;
    @Column(name = "on_road_price")
    private Double onRoadPrice;
    @Column(name = "nps_scheme")
    private Boolean npsScheme;
    @Column(name = "exchange_check")
    private Boolean exchangeCheck;
    @Column(name = "exchange_offers")
    private Double exchangeOffers;
    @Column(name = "corporate_check")
    private Boolean corporateCheck;
    @Column(name = "corporate_name")
    private String corporateName;
    @Column(name = "corporate_offer")
    private Double corporateOffer;
    @Column(name = "rural_check")
    private Boolean ruralCheck;
    @Column(name = "rural_offer")
    private Double ruralOffer;
    @Column(name = "promotional_offers")
    private Double promotionalOffers;
    @Column(name = "cash_discount")
    private Double cashDiscount;
    @Column(name = "special_scheme")
    private Double specialScheme;
    @Column(name = "foc_accessories")
    private Double focAccessories;
    @Column(name = "additional_offer1")
    private Double additionalOffer1;
    @Column(name = "additional_offer2")
    private Double additionalOffer2;
    @Column(name = "final_price")
    private Double finalPrice;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_datetime")
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_datetime")
    private Date modifiedDate;

    @ManyToOne
    @JoinColumn(name = "lead_id")
    private DmsLead dmsLead;

    @Column(name = "insurance_addon", columnDefinition = "json")
    @Type(type = "json")
    private List<InsuranceAddOn> insuranceAddonData;

    @Column(name = "offer_data", columnDefinition = "json")
    @Type(type = "json")
    private List<OfferData> offerData;

}
