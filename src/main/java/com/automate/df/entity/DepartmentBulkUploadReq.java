package com.automate.df.entity;

import lombok.Data;

@Data
public class DepartmentBulkUploadReq {
    private String departmentName;
    private Integer orgId;
    private String location;
    private Boolean status;
    private Integer branchId;
    private String branchName;

}