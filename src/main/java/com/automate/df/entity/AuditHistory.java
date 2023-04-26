package com.automate.df.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="audit_history")
@Entity
@Data
@NoArgsConstructor
public class AuditHistory {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="STAGE_NAME")
	String stageName;
	
	@Column(name="ORG_ID")
	String orgId;
	
	@Column(name="TASK_NAME")
	String taskName;
	
	@Column(name="LEAD_ID")
	String leadId;
	
	@Column(name="UNIVERSAL_ID")
	String universalId;
	
	@Column(name="DATA")
	String data;
	
	@Column(name="UPDATED_EMP_ID")
	String updatedEmpID;
	

	@Column(name="UPDATED_EMP_NAME")
	String updatedEmpName;
	

	@Column(name="UPDATED_AT")
	String updatedAt;
	
	
	
}
