package com.automate.df.model.sales.workflow;

import com.automate.df.model.sales.master.DmsRoleDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskCategory {

    private int id;

    private String createdDate;

    private String description;

    private String executionType;

    private Boolean isActive;

    private String taskCategory;

    private String taskType;

    private DmsRoleDto assigneeRole;

    private DmsRoleDto approverRole;

    private DmsRoleDto escalationRole;

    private Boolean approvalType;


}
