package com.automate.df.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.automate.df.entity.OrgVerticalLocationPageGroup;

public interface OrgVerLocPageGroupRepo extends JpaRepository<OrgVerticalLocationPageGroup, Integer> {
 
	OrgVerticalLocationPageGroup findByName(String name);
	
	OrgVerticalLocationPageGroup findByNameAndParentPageId(String name,int parentPageId);
	
	List<OrgVerticalLocationPageGroup> findAllByParentPageId(int pageId);
}
