package com.automate.df.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter

public class OfferMapping {


    private String id;

    private String offerId;

    private String vehicleId;
    private String varientId;
    private String organisationId;
    private String type;


    public enum Type {
        GENERAL,
        CORPORATE
    }


}
