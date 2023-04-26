package com.automate.df.entity;

import java.io.Serializable;

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
@Table(name = "sub_lost_reasons")
public class SubLostReasons implements Serializable {
	private static final long serialVersionUID = 4895908114629386216L;
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name="stage_name")
	private String stageName;
	@Column(name="lost_reason")
	private String lostReason;
	@Column(name="lostreason_id")
	private String lostreasonId;
	@Column(name="sub_reason")
	private String subReason;
	private String status;
	private String orgId;
	private String bulkUploadId;
	private String createdBy;
	private String updatedBy;
	private String createdAt;
	private String updatedAt;
}