package com.automate.df.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.automate.df.entity.AutomateFileExtract;

public interface AutFileExtractRepo extends JpaRepository<AutomateFileExtract, Integer> {
 
	AutomateFileExtract findByName(String name);
	
	AutomateFileExtract findByPageIdentifier(String pageIdentifier);
	
	List<AutomateFileExtract> findByBusinessUnit(String businessUnit);
	
}
