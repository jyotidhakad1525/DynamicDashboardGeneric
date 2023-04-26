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

@Table(name="organisation_vertical_location_role")
@Entity
@Data
@NoArgsConstructor
public class OrgVerticalLocationRole {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="UUID")
	String UUID;
	
	@Column(name="role_id")
	int roleId;
	
	@Column(name="name")
	String name;
	
	@Column(name="created_at")
	Timestamp createdAt;
	
	@Column(name="updated_at")
	Timestamp updated_at;
	
	@OneToMany(targetEntity = OrgVerticalLocationRoleMenu.class,fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="org_ver_loc_role_id")
	private List<OrgVerticalLocationRoleMenu> roleMenuList;
}
