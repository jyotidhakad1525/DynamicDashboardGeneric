package com.automate.df.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="audit_master")
@Entity
@Data
@NoArgsConstructor
public class AuditMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="ACTIVITY_NAME")
	String activityName;
	
	@Column(name="TASK_NAME")
	String taskName;
	

	@Column(name="ACTIVITY_TYPE")
	String activityType;
	
	
	@Column(name="ACTIVE")
	String active;
	

	
}
