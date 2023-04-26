package com.automate.df.entity.sales.lead;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Setter
@Getter
@Entity
@Table(name = "dms_performance_base_allocation")
@NamedQuery(name = "DmsPerformanceBaseAllocation.findAll", query = "SELECT d FROM DmsPerformanceBaseAllocation d")
public class DmsPerformanceBaseAllocation implements Serializable {
    private static final long serialVersionUID = 1L;

    private BigDecimal actual;

    @Id
    @Column(name = "hrms_emp_id")
    private String hrmsEmpId;
}