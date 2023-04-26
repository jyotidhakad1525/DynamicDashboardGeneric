package com.automate.df.model.sales.lead;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DmsAddressDto {

    private int id;
    private String pincode;
    private String addressType;
    private String village;
    private String city;
    private String district;
    private String state;
    private String country;
    private String houseNo;
    private String street;
    private String latitude;
    private String longitude;
    private String preferredBillingAddress;
    private Boolean isRural;
    private Boolean isUrban;


    public Boolean getRural() {
        return isRural;
    }

    public void setRural(Boolean rural) {
        isRural = rural;
    }

    public Boolean getUrban() {
        return isUrban;
    }

    public void setUrban(Boolean urban) {
        isUrban = urban;
    }
}
