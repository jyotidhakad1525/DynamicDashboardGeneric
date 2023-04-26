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

@Table(name = "bulkupload_template_location")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkUploadTemplate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "page_id")
	int pageId;

	@Column(name = "org_id")
	int orgId;

	@Column(name = "template_location_path")
	String templateLocationPath;

	@Column(name = "created_at")
	String createdAt;

	@Column(name = "updated_at")
	String updatedAt;
}
