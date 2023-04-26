package com.automate.df.model.sales.lead;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Setter
@Getter
public class DmsOnRoadPriceDto {

    private int id;
    private int lead_id;
    private Double exShowroomPrice;
    private Double lifeTax;
    private Double registrationCharges;
    private String insuranceType;
    private Double insuranceAddOn;
    private String warrantyName;
    private Double warrantyAmount;
    private Double handlingCharges;
    private Double essentialKit;
    private Double tcs;
    private String dmsOnroadPriceCol;
    private Double onRoadPrice;
    private Boolean npsScheme;
    private Boolean exchangeCheck;
    private Double exchangeOffers;
    private Boolean corporateCheck;
    private String corporateName;
    private Double corporateOffer;
    private Boolean ruralCheck;
    private Double ruralOffer;
    private Double promotionalOffers;
    private Double cashDiscount;
    private Double specialScheme;
    private Double focAccessories;
    private Double additionalOffer1;
    private Double additionalOffer2;
    private Double finalPrice;
    private Date createdDatetime;
    private Date modifiedDatetime;
    private List<InsuranceAddOn> insuranceAddonData;
    private List<OfferData> offerData;

}
