package com.automate.df.model.sales.lead;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class DmsDeliveryCheckListDto {


    private int id;
    private String itemName;
    private Boolean checkList;
    private Date createdDateTime;
    private Date modifiedDateTime;
    private int leadId;

}
