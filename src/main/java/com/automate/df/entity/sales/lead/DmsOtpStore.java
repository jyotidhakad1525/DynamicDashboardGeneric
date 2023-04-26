package com.automate.df.entity.sales.lead;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Setter
@Getter
@Entity
@Table(name = "dms_otp_store")
@NamedQuery(name = "DmsOtpStore.findAll", query = "SELECT d FROM DmsOtpStore d")
public class DmsOtpStore implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date_time")
    private String dateTime;

    @Column(name = "is_validation_done")
    private Boolean isValidationDone;

    @Column(name = "mobile_no")
    private String mobileNo;

    private String otp;

    @Column(name = "session_key")
    private String sessionKey;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "validated_datetime")
    private Date validatedDatetime;
}