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

@Table(name="automate_file_import")
@Entity
@Data
@NoArgsConstructor
public class AutomateFileImport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name="file_name")
	String fileName;
	@Column(name="file_type")
	String fileType;
	@Column(name="file_path")
	String filePath;
	@Column(name="sequence_no")
	int sequenceNo;
	@Column(name="status")
	String status;
	@Column(name="is_flush")
	boolean isFlush;
	@Column(name="previous_import_id")
	int previousImportId;
	@Column(name="num_of_records_processed")
	int numOfRecordsProcessed;
	@Column(name="num_of_records_inserted")
	int numOfRecordsInserted;
	@Column(name="num_of_records_updated")
	int numOfRecordsUpdated;
	@Column(name="num_of_records_failed")
	int numOfRecordsFailed;

	@Column(name="import_type")
	String importType;
	@Column(name="is_bulk_import")
	boolean isBulkImport;
	
	@Column(name="created_at")
	Timestamp createdAt;
	
	@Column(name="updated_at")
	Timestamp updated_at;
	
}
