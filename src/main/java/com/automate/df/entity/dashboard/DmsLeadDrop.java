package com.automate.df.entity.dashboard;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="dms_lead_drop")
@Entity
@Data
@NoArgsConstructor
public class DmsLeadDrop {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="lead_id")
	private String leadId;
	
	@Column(name="stage")
	private String stage;
	
	
	@Column(name="created_datetime")
	private String createdDateTime;
	
	
	@Column(name="lost_reason")
	private String lostReason;
	
	
	
	@Column(name="lost_sub_reason")
	private String lostSubReason;
	
	
	
	
	
}
