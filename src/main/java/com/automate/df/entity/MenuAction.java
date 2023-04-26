package com.automate.df.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="menu_action")
@Entity
@Data
@NoArgsConstructor
public class MenuAction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="menu_identifier")
	String menuIdentifier;
	
	String action;
	
	@Column(name="action_url")
	String actionUrl;
	
	@Column(name="name")
	String name;
}
