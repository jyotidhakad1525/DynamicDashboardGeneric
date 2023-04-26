package com.automate.df.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "REPORT_QUERIES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportQueries {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="report_identifier")
	int reportId;
	
	
	@Column(name="role_identifier")
	int roleId;
	
	@Column(name="query")
	String query;
	
	@Column(name="table_name")
	String tableName;
	
	@Column(name="max_items")
	int maxItems;
	
}
