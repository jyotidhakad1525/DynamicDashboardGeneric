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

@Table(name="automate_bulk_upload")
@Entity
@Data
@NoArgsConstructor
public class AutomateBulkUpload {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name="file_extractname")
	String fileExtractName;
	@Column(name="failed_file_path")
	String failedFilePath;
	
	@Column(name="created_at")
	Timestamp createdAt;
	
	@Column(name="updated_at")
	Timestamp updated_at;
	
}
