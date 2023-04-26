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


@Table(name="organisation_vertical_location_page_group")
@Entity
@Data
@NoArgsConstructor
public class OrgVerticalLocationPageGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name="org_ver_loc_page_id")
	int parentPageId;
	@Column(name="page_group")
	int pageGroupId;
	@Column(name="name")

	String name;
	@Column(name="position")
	int position;
	@Column(name="min_items")
	int minItems;
	@Column(name="max_items")
	int maxItems;
	@Column(name="icon_cls")
	String iconClass;
	@Column(name="no_of_columns")
	int noOfCols;
	@Column(name="created_at")
	Timestamp createdAt;
	
	@Column(name="updated_at")
	Timestamp updated_at;
	
	
	
	  @OneToMany(targetEntity =
	  OrgVerticalLocationPageGroupField.class,fetch=FetchType.LAZY,cascade =
	  CascadeType.ALL)
	  @JoinColumn(name="org_acc_loc_page_group_id") 
	  private List<OrgVerticalLocationPageGroupField> fieldList;
	 
	 
}
