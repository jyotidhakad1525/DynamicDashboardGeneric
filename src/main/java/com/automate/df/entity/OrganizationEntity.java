package com.automate.df.entity;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="test_organization")
@Entity
@Data
@NoArgsConstructor

public class OrganizationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	String orgname;
	String orgdesc;
	@OneToMany(targetEntity = AddressEntity.class,cascade = CascadeType.ALL)
	@JoinColumn(name="org_id")
	List<AddressEntity> address;
}
