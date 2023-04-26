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

@Table(name="location_node_def")
@Entity
@Data
@NoArgsConstructor
public class LocationNodeDef {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="location_node_def_name")
	String locationNodeDefName;
	
	@Column(name="location_node_def_type")
	String locationNodeDefType;
	
	@Column(name="parent_id")
	int parentId;
	
	@Column(name="org_id")
	int orgId;
	

	@Column(name="created_by")
	Integer createdBy;
	
	@Column(name="created_on")
	Timestamp createdOn;

	@Column(name="modified_by")
	Integer modifiedBy;

	@Column(name="modified_on")
	Timestamp modifiedOn;
	
	@Column(name="display_name")
	String displayName;
	
	@Column(name="active")
	String active;
	
}
