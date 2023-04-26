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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "`ops`.check_list_items")
public class CheckListItems{


	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;	
	
	@Column(name = "check_list_type")
    private String checkListType ; 
	
	@Column(name = "check_list_sub_type")
    private String checkListSubType ; 
	
	@Column(name = "check_list")
    private String checkList ; 
	
	@Column(name = "status")
    private String status ; 
	
    @Column(name = "created_at") 
	private String createdAt  ; 
    
    @Column(name = "created_by") 
	private String createdBy ; 
    
    @Column(name = "updated_by") 
	private String updatedBy ; 
    
    @Column(name = "updated_at") 
	private String updatedAt ; 
    
    @Column(name = "org_id") 
	private int orgId; 

    @Column(name = "checklisttype_id") 
	private int checklisttypeId; 

}
