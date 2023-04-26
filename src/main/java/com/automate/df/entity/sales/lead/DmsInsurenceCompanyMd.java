package com.automate.df.entity.sales.lead;

import com.automate.df.enums.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@Entity
@Table(name = "dms_insurence_company_md")
@NamedQuery(name = "DmsInsurenceCompanyMd.findAll", query = "SELECT d FROM DmsInsurenceCompanyMd d")
public class DmsInsurenceCompanyMd implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private int id;

    @Column(name = "company_name")
    private String companyName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_datetime")
    private Date createdDatetime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
}