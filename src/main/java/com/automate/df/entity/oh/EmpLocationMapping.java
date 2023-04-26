package com.automate.df.entity.oh;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.automate.df.entity.dashboard.DmsLead;

import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="emp_location_mapping")
@Entity
@Data
@NoArgsConstructor
public class EmpLocationMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="location_node_data_id")
	String locationNodeDataId;
	
	@Column(name="emp_id")
	String empId;
	
	@Column(name="org_id")
	String orgId;
	@Column(name="active")
	String active;
	@Column(name="branch_id")
	Integer branchId;
}
