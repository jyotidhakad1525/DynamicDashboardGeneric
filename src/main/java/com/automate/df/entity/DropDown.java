package com.automate.df.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="dropdown_master")
@Entity
@Data
@NoArgsConstructor
public class DropDown {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="business_unit")
	private String bu;
	
	private String key;
	private String value;
	@Column(name="dropdown_type")
	private String dropdownType;
	
	private int parent;
}
