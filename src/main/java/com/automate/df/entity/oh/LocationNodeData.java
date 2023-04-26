package com.automate.df.entity.oh;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="location_node_data")
@Entity
@Data
@NoArgsConstructor
public class LocationNodeData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="cananical_name")
	String cananicalName;
	
	@Column(name="code")
	String code;
	
	@Column(name="name")
	String name;
	
	@Column(name="parent_id")
	String parentId;
	
	@Column(name="type")
	String type;
	
	@Column(name="ref_parent_id")
	String refParentId;
	
	@Column(name="org_id")
	Integer orgId;
	
	@Column(name="created_by")
	Integer createdBy;
	
	@Column(name="created_on")
	Timestamp createdOn;

	@Column(name="modified_by")
	Integer modifiedBy;

	@Column(name="modified_on")
	Timestamp modifiedOn;
	
	@Column(name="active")
	String active;
	
	@Column(name="location_node_def_id")
	Integer locationNodeDefId;
	
	@Column(name="leaf_node")
	String leafNode;
	
	
}
