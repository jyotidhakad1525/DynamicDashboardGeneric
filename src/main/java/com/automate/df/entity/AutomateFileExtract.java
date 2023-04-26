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

/**
 * 
 * @author sgogusetty
 *
 */

@Table(name="automate_file_extract")
@Entity
@Data
@NoArgsConstructor
public class AutomateFileExtract {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	String name;
	@Column(name="business_unit")
	String businessUnit;
	@Column(name="table_name")
	String tableName;
	@Column(name="validation_column")
	String validationColumn;
	@Column(name="child_table_name")
	String childTableName;
	String pojo;
	@Column(name="child_pojo")
	String childPojo;
	@Column(name="child_child_table_name")
	String childChildTableName;
	@Column(name="child_child_pojo")
	String childChildPojo;
	@Column(name="created_at")
	Timestamp createdAt;
	
	@Column(name="updated_at")
	Timestamp updated_at;
	
	@Column(name="page_identifier")
	String pageIdentifier;
}
