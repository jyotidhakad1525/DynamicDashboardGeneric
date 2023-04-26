package com.automate.df.entity;


import lombok.Data;

import java.util.List;

@Data
public class DepartmentRequest {

    private String departmentName;
    private Integer orgId;
    private String userId;
    List<DepartmentData.DepartmentSubData> departmentSubData;
}
