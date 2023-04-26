package com.automate.df.entity.sales.lead;

import com.automate.df.enums.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Setter
@Getter
@Entity
@Table(name = "dms_bank_finance_services_md")
@NamedQuery(name = "DmsBankFinanceServicesMd.findAll", query = "SELECT d FROM DmsBankFinanceServicesMd d")
public class DmsBankFinanceServicesMd implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private int id;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_type")
    private String bankType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_datetime")
    private Date createdDatetime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

}