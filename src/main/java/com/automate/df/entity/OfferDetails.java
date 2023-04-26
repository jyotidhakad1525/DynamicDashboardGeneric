package com.automate.df.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "offer_details")
public class OfferDetails{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "organization_id")
    private String organizationId;
    @Column(name = "offer_name")
    private String offerName;
    @Column(name = "amount")
    private double amount;
    @Column(name = "shot_description")
    private String shotDescription;
    @Column(name = "long_description")
    private String longDescription;
    @Column(name = "offer_type")
    private String offerType;
    @Temporal(TemporalType.DATE)    
    @Column(name = "start_date")
    private String startDate;
    @Temporal(TemporalType.DATE)    
    @Column(name = "end_date")
    private String end_date;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "small_icon_url")
    private String smallIconUrl;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "html_url")
    private String htmlUrl;

    @Column(name = "is_checkbox_allowed")
    private boolean isCheckboxAllowed;


    @Column(name = "created_by")
    private Integer createdBy;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "modified_by")
    private Integer modifiedBy;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "modified_date")
    private Date modifiedDate;
    @Column(name = "confirmation_massage")
    private String confirmationMassage;

    public boolean isCheckboxAllowed() {
        return isCheckboxAllowed;
    }

    public void setCheckboxAllowed(boolean isCheckboxAllowed) {
        this.isCheckboxAllowed = isCheckboxAllowed;
    }

    public enum Status {
        Active,
        InActive
    }
}
