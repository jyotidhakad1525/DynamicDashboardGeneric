package com.automate.df.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter

public class InsuranceVarientMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer varient_id;

    private Integer insurence_id;

    private Integer cost;

    private String status;


    private String policy_name;

    private Integer vehicleId;


    private String model;

 
    private String varientName;
    
	String sno;
	String parentIdentifier;

}
