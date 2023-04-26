package com.automate.df.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "organisation_vertical_location_role_menu")
@Entity
@Data
@NoArgsConstructor
public class OrgVerticalLocationRoleMenu {

	private static Object ID = new Object();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "org_ver_loc_role_id")
	int roleId;

	@Column(name = "menu_id")
	int menuId;

	String UUID;

	String name;

	@Column(name = "redirect_url")
	String redirectUrl;

	@Column(name = "page_identifier")
	String pageIdentifier;

	@Column(name = "parent_identifier")
	Integer parentIdentifier;

	@Column(name = "mode")
	String mode;

	@Column(name = "created_at")
	Timestamp createdAt;

	@Column(name = "updated_at")
	Timestamp updated_at;

	@Transient
	@JsonProperty
	List<OrgVerticalLocationRoleMenu> children = null;
	
	public OrgVerticalLocationRoleMenu(Integer pageIdentifer, Integer parentIdentifier, String name, String mode) {
		super();
		this.id = pageIdentifer;
		this.parentIdentifier = parentIdentifier;
		this.name = name;
		this.mode = mode;
	}

	public OrgVerticalLocationRoleMenu(Integer pageIdentifer, String name, String mode) {
		this(pageIdentifer, null, name, mode);
	}
	

	public List<OrgVerticalLocationRoleMenu> computeChildernIfAbsent() {

		if (null == children) {
			synchronized (ID) {
				children = new ArrayList<>();
			}
		}

		return children;
	}

}
