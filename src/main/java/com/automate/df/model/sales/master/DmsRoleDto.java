package com.automate.df.model.sales.master;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
public class DmsRoleDto {

    private int roleId;
    private int branchId;
    private String createdBy;
    private Timestamp creationDate;
    private String description;
    private String modifiedBy;
    private int organizationId;
    private String roleName;

}
