package com.automate.df.entity.sales.workflow;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Setter
@Getter
@Entity
@Table(name = "dms_task_track")
@NamedQuery(name = "DMSTaskTrack.findAll", query = "SELECT d FROM DMSTaskTrack d")
public class DMSTaskTrack implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_track_id")
    private int taskTrackId;

    @Column(name = "lead_id")
    private int leadId;

    @Column(name = "modified_field")
    private String modifiedField;

    @Column(name = "modified_value")
    private String modifiedValue;

    @Column(name = "task_id")
    private int taskId;

    @Column(name = "universal_id")
    private String universalId;
}