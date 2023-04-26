package com.automate.df.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

@Setter
@Getter
@JsonIgnoreProperties(value = {"status", "brochure"})
public class RoadTax implements Serializable {

    private static final long serialVersionUID = 6590817921472593861L;


    private Integer id;


    private Integer organization_id;

//    @Convert(converter = JpaJsonDocumentsMapConverter.class)
//    @Column(name = "tax_calculation", columnDefinition = "TEXT", nullable = true)
    private Map<String, Object> tax_calculation;
}
