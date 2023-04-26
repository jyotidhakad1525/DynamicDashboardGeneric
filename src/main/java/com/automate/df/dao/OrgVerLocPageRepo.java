package com.automate.df.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.automate.df.entity.OrgVerticalLocationPage;

public interface OrgVerLocPageRepo extends JpaRepository<OrgVerticalLocationPage, Integer> {
 
	OrgVerticalLocationPage findByName(String name);
	
	OrgVerticalLocationPage findByNameAndUUID(String name,String uuid);
	
	List<OrgVerticalLocationPage> findByUUID(String uuid);
	
}
