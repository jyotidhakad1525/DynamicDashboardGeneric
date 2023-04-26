package com.automate.df.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class DmsAccessoriesDto {

    private int id;

    private Double amount;

    private String accessoriesName;

    private int leadId;

    private Boolean allotmentStatus;
    
    private String dmsAccessoriesType;
}
