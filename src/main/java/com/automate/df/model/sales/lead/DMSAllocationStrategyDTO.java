package com.automate.df.model.sales.lead;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class DMSAllocationStrategyDTO {

    private int id;
    private String name;
    private String description;
    private boolean isActive;
    private Date createdTime;
    private Date updatedTime;
    private String createdBy;
    private String updatedBy;
    private int orgId;
    private String status;
    private String approvedBy;
    private int branchId;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }


}
