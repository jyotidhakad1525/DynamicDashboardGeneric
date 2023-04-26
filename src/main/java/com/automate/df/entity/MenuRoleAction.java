package com.automate.df.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="menu_role_action")
@Entity
@Data
@NoArgsConstructor
public class MenuRoleAction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="role_identifier")
	int roleIdentifier;
	

	@Column(name="menu_action_identifier")
	int menuActionIdentifier;
	
}
