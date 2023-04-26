package com.automate.df.entity;

import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.automate.df.model.JpaJsonDocumentsListConverter;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "extended_warranty")
public class ExtendedWaranty {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "organization_id")
    private String organization_id;
    @Column(name = "vehicle_id")
    private String vehicle_id;
    @Column(name = "varient_id")
    private String varient_id;
    @Convert(converter = JpaJsonDocumentsListConverter.class)
    @Column(name = "warranty", columnDefinition = "TEXT", nullable = true)
    private List<HashMap<String, Object>> warranty;
    
}