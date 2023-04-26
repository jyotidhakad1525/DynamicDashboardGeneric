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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="page_group_field_role_access")
@Entity
@Data
@NoArgsConstructor
public class PageGroupFieldRoleAccess {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	/*@OneToMany(targetEntity = OrgVerticalLocationPage.class,fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="id")
	
	
	@OneToOne(targetEntity = OrgVerticalLocationPageGroup.class,fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="id")
	private OrgVerticalLocationPageGroup locationPageGroupList;
	
	@OneToMany(targetEntity = OrgVerticalLocationPageGroupField.class,fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="id")
	private List<OrgVerticalLocationPageGroupField> locationPageGroupFieldList;*/
	
	@Column(name="org_ver_loc_page_id")
	int pageId;
	
	@Column(name="org_ver_loc_page_group_id")
	int groupId;
	
	@Column(name="org_ver_loc_page_group_field_id")
	int fieldId;
	
	@Column(name="org_ver_loc_role_id")
	int roleId;
	
	@Column(name="permission_type")
	String permissionType;
	
	@Column(name="mode")
	String mode;
	
	@Column(name="created_at")
	Timestamp createdAt;
	
	@Column(name="updated_at")
	Timestamp updated_at;
}
