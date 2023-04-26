package com.automate.df.entity.sales.lead;


import com.automate.df.entity.dashboard.DmsLead;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Setter
@Getter
@Entity
@Table(name = "dms_accessories")
@NamedQuery(name = "DmsAccessories.findAll", query = "SELECT d FROM DmsAccessories d")
public class DmsAccessories implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "accessories_name")
    private String accessoriesName;

    @Column(name = "allotement_status")
    private Boolean allotementStatus;
    
    @Column(name = "dms_accessories_type")
    private String dmsAccessoriesType;

    @ManyToOne
    @JoinColumn(name = "lead_id", referencedColumnName = "id")
    private DmsLead dmsLead;

}
