package com.automate.df.entity.sales.lead;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the dms_sms_store database table.
 */
@Setter
@Getter
@Entity
@Table(name = "dms_sms_store")
@NamedQuery(name = "DmsSmsStore.findAll", query = "SELECT d FROM DmsSmsStore d")
public class DmsSmsStore implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String mobile;

    @Column(name = "send_date_time")
    private String sendDateTime;

    @Column(name = "sms_data")
    private String smsData;

    private String status;
}