package com.automate.df.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.automate.df.entity.LostReasons;
import org.springframework.data.repository.query.Param;

public interface LostSubLostRepository extends CrudRepository<LostReasons, Integer> {
	 @Query(value = "SELECT * FROM lost_reasons WHERE org_id =?1 and stage_name=?2 and status='Active'", nativeQuery = true)
	    List<LostReasons> getAllSubLost(String orgId,String stageName);

	@Query(value = "select * from lost_reasons where lost_reason=:lost_reason and org_id =:org_id and stage_name=:stage_name", nativeQuery = true)
	List<LostReasons> getAllSubLost(@Param("lost_reason") String lost_reason,
									@Param("org_id") Integer org_id,
									@Param("stage_name") String stage_name);
}
