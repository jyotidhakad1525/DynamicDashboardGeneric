package com.automate.df.model.sales.lead;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DmsEmployeeAllocationDto {

    private int id;
    private String branchId;
    private String createdBy;
    private Date createddatetime;
    private Date dateOfAllocation;
    private int employeeId;
    private String employeeName;
    private Date modifiedDate;
    private String orgId;
    private String status;

    public DmsEmployeeAllocationDto() {
    }

}
