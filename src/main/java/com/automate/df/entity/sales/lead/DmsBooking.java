package com.automate.df.entity.sales.lead;

import com.automate.df.entity.dashboard.DmsLead;
import com.automate.df.enums.ModeOfPayment;
import com.automate.df.enums.Payment;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Setter
@Getter
@Entity
@Table(name = "dms_booking")
@NamedQuery(name = "DmsBooking.findAll", query = "SELECT d FROM DmsBooking d")
public class DmsBooking implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "booking_amount")
    private Double bookingAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_at")
    private Payment paymentAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode_of_payment")
    private ModeOfPayment modeOfPayment;

    @Column(name = "other_vehicle")
    private Boolean otherVehicle;

    @Column(name = "add_on_covers")
    private String addOnCovers;

    @Column(name = "warranty")
    private String warranty;

    @Column(name = "nps_scheme")
    private Boolean npsScheme;

    @Column(name = "exchange_offer")
    private Boolean exchangeOffer;

    @Column(name = "corporate_name")
    private String corporateName;

    @Column(name = "corporate_offer")
    private Boolean corporateOffer;

    @Column(name = "rural_offer")
    private Boolean ruralOffer;

    @Column(name = "promotional_offers")
    private Double promotionalOffers;

    @Column(name = "cash_discount")
    private Double cashDiscount;

    @Column(name = "foc_accessories")
    private Double focAccessories;

    @Column(name = "add_offer1")
    private Double addOffer1;

    @Column(name = "add_offer2")
    private Double addOffer2;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "reason_vehicle_allocation")
    private String reasonVehicleAllocation;

    @Column(name = "special_scheme")
    private Double specialScheme;

    @Column(name = "delivery_location")
    private String deliveryLocation;

    @Column(name = "received_booking_amount")
    private String receivedBookingAmount;

    @Column(name = "total_paid")
    private Double totalPaid;

    @Column(name = "vinno")
    private String vinNo;

    @Column(name = "engineno")
    private String engineNo;

    @Column(name = "model")
    private int model;

    @Column(name = "varient")
    private int varient;

    @Column(name = "fuel")
    private String fuel;

    @Column(name = "color")
    private int color;

    @Column(name = "chassisno")
    private String chassisNo;

    @Column(name = "keyno")
    private String keyNo;

    @Column(name = "insurance_type")
    private String insuranceType;

    //bi-directional many-to-one association to DmsLead
    @ManyToOne
    @JoinColumn(name = "lead_id")
    private DmsLead dmsLead;
}
