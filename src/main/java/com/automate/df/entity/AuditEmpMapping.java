package com.automate.df.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="audit_emp_mapping")
@Entity
@Data
@NoArgsConstructor
public class AuditEmpMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="AUDIT_WF_ID")
	Integer auditWfId;
	
	
	
	@Column(name="ORG_ID")
	Integer orgId;
	
	@Column(name="UPDATED_BY")
	String  updatedBy;
	
	@Column(name="UPDATED_AT")
	String updatedAt;
}
