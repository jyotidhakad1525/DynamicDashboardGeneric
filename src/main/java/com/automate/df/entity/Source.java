package com.automate.df.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "dms_source_of_enquiries")
public class Source implements Serializable {
    private static final long serialVersionUID = 4895908114629386216L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;	
    private String name ;  
    private String value ;  
    private String description ; 
	@Column(name = "created_at") 
	private String createdAt  ; 
	@Column(name = "updated_at") 
	private String updatedAt ; 
	@Column(name = "bulk_upload_id") 
	private String bulkUploadId; 
	@Column(name = "created_by") 
	private String created_by ; 
	@Column(name = "updated_by") 
	private String updated_by ; 
	private String status ; 
	@Column(name = "is_active") 
	private int isActive ; 
	@Column(name = "org_id") 
	private String orgId; 
	@OneToMany(targetEntity = SubSource.class, cascade = CascadeType.ALL) 
    @JoinColumn(name = "source_id", referencedColumnName = "id") 
    @Fetch(FetchMode.JOIN)
    private Set<SubSource> subsource;
}
