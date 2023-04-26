package com.automate.df.entity.oh;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="dms_grade")
@Entity
@Data
@NoArgsConstructor
public class DmsGrade {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	Integer id;
	
	@Column(name="level_name")
	private String levelName;
	
	@Column(name="level")
	private Integer level;
}
