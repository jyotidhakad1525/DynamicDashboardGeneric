package com.automate.df.entity.sales.lead;

import com.automate.df.entity.dashboard.DmsLead;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Setter
@Getter
@Entity
@Table(name = "dms_lead_score_card")
@NamedQuery(name = "DmsLeadScoreCard.findAll", query = "SELECT d FROM DmsLeadScoreCard d")
public class DmsLeadScoreCard implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String brand;

    private String color;

    @Column(name = "customer_from")
    private String customerFrom;

    @Column(name = "dealership_location")
    private String dealershipLocation;

    @Column(name = "dealership_name")
    private String dealershipName;

    @Column(name = "decision_pending_reason")
    private String decisionPendingReason;

    private String dist;

    @Column(name = "dist_hq")
    private String distHq;

    private String fuel;

    private String hamlet;

    @Column(name = "looking_for_any_other_brand")
    private Boolean lookingForAnyOtherBrand;

    private String make;

    private String mandal;

    @Column(name = "mandal_hq")
    private String mandalHq;

    private String model;

    @Column(name = "on_road_priceany_difference")
    private String onRoadPriceanyDifference;

    @Column(name = "other_make")
    private String otherMake;

    @Column(name = "other_model")
    private String otherModel;

    @Column(name = "price_range")
    private String priceRange;

    private String town;

    private String variant;

    private String village;

    @Column(name = "voiceof_customer_remarks")
    private String voiceofCustomerRemarks;

    @ManyToOne
    @JoinColumn(name = "lead_id")
    private DmsLead dmsLead;

}