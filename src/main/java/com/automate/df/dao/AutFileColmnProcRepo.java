package com.automate.df.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.automate.df.entity.AutomateFileColumnProcessor;

public interface AutFileColmnProcRepo extends JpaRepository<AutomateFileColumnProcessor, Integer> {
 
	
	List<AutomateFileColumnProcessor> findByFileExtractId(int fileExtractId);
	
}
