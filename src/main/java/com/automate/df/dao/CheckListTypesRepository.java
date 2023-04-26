package com.automate.df.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.automate.df.entity.CheckListType;

public interface CheckListTypesRepository extends CrudRepository<CheckListType, Integer> {
	 @Query(value = "SELECT * FROM ops.check_list_type WHERE org_id =:orgId and status='Active'", nativeQuery = true)
	    List<CheckListType> getAllCheckListType(int orgId);
}
