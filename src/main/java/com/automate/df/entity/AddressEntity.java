package com.automate.df.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="test_address")
@Entity
@Data
@NoArgsConstructor
public class AddressEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	String city;
	String state;
	@Column(name="bulk_upload_id")
	int bulkUploadId;
	
	@OneToMany(targetEntity = ContactEntity.class,cascade = CascadeType.ALL)
	@JoinColumn(name="address_id")
	List<ContactEntity> contacts;
}
