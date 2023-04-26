package com.automate.df.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author srujan
 *
 */

@Table(name="organisation_vertical_location_page")
@Entity
@Data
@NoArgsConstructor
public class OrgVerticalLocationPage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	String UUID;
	@Column(name="page_id")
	int pageId;
	String name;
	@Column(name="no_of_columns")
	int noOfClns;
	String pojo;
	@Column(name="page_json")
	String json;
	@Column(name="position")
	int position;
	@Column(name="end_point")
	String endPoint;
	@Column(name="created_at")
	Timestamp createdAt;
	
	@Column(name="updated_at")
	Timestamp updated_at;
	
	@OneToMany(targetEntity = OrgVerticalLocationPageGroup.class,fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="org_ver_loc_page_id")
	private List<OrgVerticalLocationPageGroup> groupList;
}
