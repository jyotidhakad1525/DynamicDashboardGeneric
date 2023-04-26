package com.automate.df.entity.sales.lead;

import com.automate.df.entity.dashboard.DmsLead;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "dms_delivery_check_list")
@NamedQuery(name = "DmsDeliveryCheckList.findAll", query = "SELECT d FROM DmsDeliveryCheckList d")
public class DmsDeliveryCheckList implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "check_list")
    private Boolean checkList;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_datetime")
    private Date createdDateTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_datetime")
    private Date modifiedDateTime;

    @ManyToOne
    @JoinColumn(name = "lead_id")
    private DmsLead dmsLead;

}
