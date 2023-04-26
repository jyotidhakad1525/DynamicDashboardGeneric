package com.automate.df.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.automate.df.entity.Source;

public interface SourceSubSourceRepository extends CrudRepository<Source, Integer> {
	 @Query(value = "SELECT * FROM dms_source_of_enquiries WHERE org_id =?1 and status='Active'", nativeQuery = true)
	    List<Source> getAllSubsource(String orgId);
}
