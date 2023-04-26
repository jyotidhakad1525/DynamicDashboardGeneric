package com.automate.df.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="auto_save")
@Entity
@Data
@NoArgsConstructor
public class AutoSaveEntity {
	
	
	
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name="universal_id")
	String universalId;
	@Column(name="data")
	String data;
	@Column(name="status")
	
	String status;

}
