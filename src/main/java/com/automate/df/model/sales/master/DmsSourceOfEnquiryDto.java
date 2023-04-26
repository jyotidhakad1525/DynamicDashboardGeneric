package com.automate.df.model.sales.master;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DmsSourceOfEnquiryDto {

    private int id;
    private String name;
    private String value;
    private String description;
    private boolean isActive;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }


}
