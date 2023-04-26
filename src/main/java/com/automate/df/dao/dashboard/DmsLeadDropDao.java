package com.automate.df.dao.dashboard;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.automate.df.entity.dashboard.DmsLead;
import com.automate.df.entity.dashboard.DmsLeadDrop;

public interface DmsLeadDropDao extends JpaRepository<DmsLeadDrop, Integer>{

	@Query(value = "SELECT lead_id FROM dms_lead_drop where lead_id in(:leadId)  and stage =:stage", nativeQuery = true)
	List<Integer> getLeads(@Param(value = "leadId") List<Integer> leadId,
			@Param(value = "stage") String stage);

	@Query(value = "SELECT * FROM dms_lead_drop where lead_id =:leadId", nativeQuery = true)
	List<DmsLeadDrop> getByLeadId(@Param(value = "leadId") int id);

	
	
	
}
