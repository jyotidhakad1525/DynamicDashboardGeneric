package com.automate.df.entity.sales.employee;

import com.automate.df.entity.dashboard.DmsLead;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Setter
@Getter
@Entity
@Table(name = "dms_exchange_buyer")
@NamedQuery(name = "DmsExchangeBuyer.findAll", query = "SELECT d FROM DmsExchangeBuyer d")
public class DmsExchangeBuyer implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String brand;

    @Column(name = "buyer_type")
    private String buyerType;

    @Column(name = "cc_clearence_expenses")
    private String ccClearenceExpenses;

    @Column(name = "challans_amount")
    private BigDecimal challansAmount;

    @Column(name = "challans_pending")
    private String challansPending;

    private String color;

    @Column(name = "expected_price")
    private BigDecimal expectedPrice;

    @Column(name = "final_price")
    private BigDecimal finalPrice;

    @Column(name = "fuel_type")
    private String fuelType;

    private String hypothication;

    @Column(name = "hypothication_branch")
    private String hypothicationBranch;

    @Column(name = "hypothication_requirement")
    private Boolean hypothicationRequirement;

    @Column(name = "insurance_available")
    private String insuranceAvailable;

    @Column(name = "insurance_company_name")
    private String insuranceCompanyName;

    @Column(name = "insurance_document_available")
    private Boolean insuranceDocumentAvailable;

    @Column(name = "insurance_document_key")
    private String insuranceDocumentKey;

    @Column(name = "insurance_document_path")
    private String insuranceDocumentPath;

    @Temporal(TemporalType.DATE)
    @Column(name = "insurance_expiry_date")
    private Date insuranceExpiryDate;

    @Column(name = "insurance_from_date")
    private String insuranceFromDate;

    @Column(name = "insurance_to_date")
    private String insuranceToDate;

    @Column(name = "insurance_type")
    private String insuranceType;

    @Column(name = "kilo_meters")
    private String kiloMeters;

    @Column(name = "loan_amount_due")
    private String loanAmountDue;

    private String loancompletion;

    private String model;

    @Column(name = "noc_clearnce_expenses")
    private String nocClearnceExpenses;

    @Column(name = "noof_owners")
    private String noofOwners;

    @Column(name = "offered_price")
    private BigDecimal offeredPrice;

    @Column(name = "other_expensess_hypothication")
    private String otherExpensessHypothication;

    @Column(name = "reg_document_key")
    private String regDocumentKey;

    @Column(name = "reg_document_path")
    private String regDocumentPath;

    @Column(name = "reg_no")
    private String regNo;

    @Column(name = "registration_date")
    private String registrationDate;

    @Column(name = "registration_validity_date")
    private String registrationValidityDate;

    private String transmission;

    private String varient;

    @Column(name = "yearof_manufacture")
    private String yearofManufacture;

    @ManyToOne
    @JoinColumn(name = "lead_id")
    private DmsLead dmsLead;
    
    
    @Column(name = "evaluation_status")
    private String evaluationStatus;
    
    

}