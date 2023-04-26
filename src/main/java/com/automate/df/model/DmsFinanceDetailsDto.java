package com.automate.df.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class DmsFinanceDetailsDto {

    private int id;
    private String annualIncome;
    private int downPayment;
    private String expectedTenureYears;
    private String financeCategory;
    private String financeCompany;
    private String financeType;
    private int loanAmount;
    private String location;
    private String rateOfInterest;
    private Double emi;

}
