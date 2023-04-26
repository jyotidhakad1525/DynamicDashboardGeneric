package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskCategory {
    public int id;
    public String createdDate;
    public String description;
    public Object executionType;
    public boolean isActive;
    public String taskCategory;
    public String taskType;
    public AssigneeRole assigneeRole;
    public ApproverRole approverRole;
    public EscalationRole escalationRole;
    public Object approvalType;
}
