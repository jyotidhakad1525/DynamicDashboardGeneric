package com.automate.df.entity;


import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "ops.evalution_parameters")
public class EvaluationParameters {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	
	@Column(name="type")
	String type;
	
	@Column(name="items")
	String items;
	
	@Column(name="status")
	String status;
	
	@Column(name="org_id")
	String orgId;
	
	@Column(name="created_by")
	String createdBy;
	
	@Column(name="updated_by")
	String updatedBy;
	
	@Column(name="created_at")
	String createdAt;
	
	@Column(name="updated_at")
	String updatedAt;
}