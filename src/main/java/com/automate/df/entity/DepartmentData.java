package com.automate.df.entity;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class DepartmentData {
    String departmentName;
    Set<String> locationList;
    Set<String> branchList;
    List<DepartmentSubData> departmentSubData;

    @Data
    public static class DepartmentSubData{
        private Integer id;
        private String location;
        private Boolean status;
        private Integer branchId;
        private String branchName;
    }
}
