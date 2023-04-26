package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApproverRole {
    public int roleId;
    public int branchId;
    public String createdBy;
    public Object creationDate;
    public String description;
    public String modifiedBy;
    public int organizationId;
    public String roleName;
}
