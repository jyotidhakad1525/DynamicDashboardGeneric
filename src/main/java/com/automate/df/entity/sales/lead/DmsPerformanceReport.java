package com.automate.df.entity.sales.lead;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;


@Setter
@Getter
@Entity
@Table(name = "dms_performance_report")
@NamedQuery(name = "DmsPerformanceReport.findAll", query = "SELECT d FROM DmsPerformanceReport d")
public class DmsPerformanceReport implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private BigInteger actual;

    @Column(name = "asking_rate")
    private int askingRate;

    @Column(name = "emp_name")
    private String empName;

    @Column(name = "hrms_emp_id")
    private String hrmsEmpId;

    @Column(name = "lead_stage")
    private String leadStage;

    private String model;

    private String month;

    private BigInteger shortfall;

    private BigInteger target;

}