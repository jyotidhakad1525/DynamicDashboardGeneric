package com.automate.df.model.sales.master;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class DmsRoleSourceEnquiryMappingDto {

    private int id;
    private int roleId;
    private List<Integer> enquirySourceIds;
    private boolean isActive;
    private String createdBy;
    private String updatedBy;
    private int enquirySourceId;


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

}
