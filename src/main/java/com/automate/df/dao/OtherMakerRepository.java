package com.automate.df.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.automate.df.entity.OtherMaker;



public interface OtherMakerRepository extends CrudRepository<OtherMaker, Integer> {
	 @Query(value = "SELECT * FROM dms_othermaker WHERE org_id =?1 and status='Active'", nativeQuery = true)
	    List<OtherMaker> getAllOtherModels(String orgId);

}
