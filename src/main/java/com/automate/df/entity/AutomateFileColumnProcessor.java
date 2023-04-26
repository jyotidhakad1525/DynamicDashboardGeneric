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

@Table(name="automate_file_column_processor")
@Entity
@Data
@NoArgsConstructor
public class AutomateFileColumnProcessor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name="aut_file_extract_id")
	int fileExtractId;
	@Column(name="column_name")
	String columnName;
	@Column(name="sequence_no")
	int sequenceNum;
	@Column(name="processor_type")
	String processorType;
	@Column(name="processor_data_attributes")
	String processorDataAttributes;
	@Column(name="table_column")
	String tableColumn;
	@Column(name="pojo_attribute")
	String pojoAttribute;
	@Column(name="required")
	String required;
	@Column(name="max_length")
	Integer maxLength;
	@Column(name="created_at")
	Timestamp createdAt;
	
	@Column(name="updated_at")
	Timestamp updated_at;
	
	@Column(name="validation_column")
	String validationColumn;
	
}
