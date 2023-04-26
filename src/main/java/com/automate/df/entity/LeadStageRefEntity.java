package com.automate.df.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Table(name="dms_lead_stage_ref")
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class LeadStageRefEntity {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="org_id")
	private Integer orgId;
	
	@Column(name="branch_id")
	private Integer branchId;
	
	
	@Column(name="lead_id")
	private Integer leadId;
	
	
	@Column(name="stage_name")
	private String stageName;
	
	@Column(name="start_date")
	private Timestamp startDate;
	
	@Column(name="end_date")
	private Timestamp endDate;
	
	@Column(name="ref_no")
	private String refNo;
	
	@Column(name="lead_status")
	private String leadStatus;
	
	@Column(name="universal_id")
	private String universalId;
}
