package com.automate.df.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.automate.df.entity.OrgVerticalLocationPageGroupField;

public interface OrgVerLocPageGrpFieldRepo extends JpaRepository<OrgVerticalLocationPageGroupField, Integer> {
 
	OrgVerticalLocationPageGroupField findByName(String name);
	
	List<OrgVerticalLocationPageGroupField> findAllByPageGroupId(int pageGroupId);
	
	OrgVerticalLocationPageGroupField findByFieldIdAndPageGroupId(int fieldId,int pageGroupId);
	
}
