package com.automate.df.entity.sales.lead;

import com.automate.df.entity.dashboard.DmsLead;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Setter
@Getter
@Entity
@Table(name = "dms_finance_details")
@NamedQuery(name = "DmsFinanceDetail.findAll", query = "SELECT d FROM DmsFinanceDetail d")
public class DmsFinanceDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "annual_income")
    private String annualIncome;

    @Column(name = "down_payment")
    private int downPayment;

    @Column(name = "expected_tenure_years")
    private String expectedTenureYears;

    @Column(name = "finance_category")
    private String financeCategory;

    @Column(name = "finance_company")
    private String financeCompany;

    @Column(name = "finance_type")
    private String financeType;

    @Column(name = "loan_amount")
    private int loanAmount;

    private String location;

    @Column(name = "rate_of_interest")
    private String rateOfInterest;

    @Column(name = "emi")
    private Double emi;

    @ManyToOne
    @JoinColumn(name = "lead_id")
    private DmsLead dmsLead;

}