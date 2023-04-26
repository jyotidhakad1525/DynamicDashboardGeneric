package com.automate.df.entity.sales.workflow;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Setter
@Getter
@Entity
@Table(name = "dms_task_history")
@NamedQuery(name = "DMSTaskHistory.findAll", query = "SELECT d FROM DMSTaskHistory d")
public class DMSTaskHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_history_id")
    private int taskHistoryId;

    @Column(name = "approver_id")
    private String approverId;

    @Column(name = "assignee_id")
    private String assigneeId;

    @Column(name = "error_detail")
    private String errorDetail;

    @Column(name = "is_error")
    private Boolean isError;

    @Column(name = "lead_id")
    private int leadId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "task_created_time")
    private Date taskCreatedTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "task_end_time")
    private Date taskEndTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "task_expected_end_time")
    private Date taskExpectedEndTime;

    @Column(name = "task_id")
    private int taskId;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "task_status")
    private String taskStatus;

    @Column(name = "task_type")
    private String taskType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "task_update_time")
    private Date taskUpdateTime;

    @Column(name = "universal_id")
    private String universalId;
}