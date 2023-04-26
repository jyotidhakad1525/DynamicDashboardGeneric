package com.automate.df.dao;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.automate.df.entity.CheckListItems;

public interface CheckListItemsRepository extends CrudRepository<CheckListItems, Integer> {
	 @Query(value = "SELECT * FROM ops.check_list_items WHERE checklisttype_id =:checklisttypeId and status='Active'", nativeQuery = true)
	    Set<CheckListItems> getByChecklisttypeId(int checklisttypeId);
}
