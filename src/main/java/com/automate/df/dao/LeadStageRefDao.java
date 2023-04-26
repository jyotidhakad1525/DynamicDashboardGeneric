package com.automate.df.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.automate.df.entity.LeadStageRefEntity;

public interface LeadStageRefDao extends JpaRepository<LeadStageRefEntity, Integer> {

	@Query(value="select * from dms_lead_stage_ref where stage_name=:stage and start_date in (select max(start_date) from dms_lead_stage_ref)",nativeQuery = true)
	List<LeadStageRefEntity> getRecentRefByStage(@Param(value="stage") String stage);
	
	@Query(value="SELECT * FROM dms_lead_stage_ref WHERE lead_id IN (:leadIdList) AND start_date>=:startDate and start_date<=:endDate",nativeQuery = true)
	List<LeadStageRefEntity> getLeadsByStageandDate(
			@Param(value="leadIdList") List<Integer> leadIdList,
			@Param(value="startDate") String startDate,@Param(value="endDate") String endDate
			);

	@Query(value="select * from dms_lead_stage_ref where lead_id=:id",nativeQuery = true)
	List<LeadStageRefEntity> getLeadStagesById(@Param(value="id")  String id);
	
	
	@Query(value="SELECT * FROM dms_lead_stage_ref WHERE lead_id IN (:leadIdList) AND start_date>=:startDate and start_date<=:endDate and stage_name='BOOKING'",nativeQuery = true)
	List<LeadStageRefEntity> getLeadsByBookingStage(
			@Param(value="leadIdList") List<Integer> leadIdList,
			@Param(value="startDate") String startDate,@Param(value="endDate") String endDate
			);
	
	
	@Query(value="select * from dms_lead_stage_ref where lead_id=:leadId and stage_name in ('INVOICE','PREDELIVERY','DELIVERY')",nativeQuery = true)
	List<LeadStageRefEntity> verifyActiveBooking(@Param(value="leadId")  Integer leadId);

	@Query(value="SELECT * FROM dms_lead_stage_ref WHERE lead_id IN (:leadIdList) AND start_date>=:startDate and start_date<=:endDate and stage_name='DELIVERY'",nativeQuery = true)
	List<LeadStageRefEntity> getLeadsByDeliveryStage(
			@Param(value="leadIdList") List<Integer> leadIdList,
			@Param(value="startDate") String startDate,@Param(value="endDate") String endDate
			);
	
	
	@Query(value="SELECT * FROM dms_lead_stage_ref WHERE lead_id IN (:leadIdList) AND stage_name=:stage AND start_date>=:startDate and start_date<=:endDate",nativeQuery = true)
	List<LeadStageRefEntity> getLeadsByBookingStage(
			@Param(value="leadIdList") List<Integer> leadIdList,
			@Param(value="stage") String stage,
			@Param(value="startDate") String startDate,@Param(value="endDate") String endDate
			);

	@Query(value="select * from dms_lead_stage_ref where stage_name = :stage and start_date>=:startDate and start_date <=:endDate and org_id=:orgId",nativeQuery = true)
	List<LeadStageRefEntity> getLeadsBasedOnStage(
			@Param(value="orgId") String orgId, 
			@Param(value="startDate") String startDate, 
			@Param(value="endDate") String endDate,
			@Param(value="stage") String stage);
	
	@Query(value="select * from dms_lead_stage_ref where stage_name = :stage and start_date>=:startDate and start_date <=:endDate and org_id=:orgId and branch_id in (:branchIdList)",nativeQuery = true)
	List<LeadStageRefEntity> getLeadsBasedOnStageBranch(
			@Param(value="orgId") String orgId, 
			@Param(value="startDate") String startDate, 
			@Param(value="endDate") String endDate,
			@Param(value="stage") String stage,
			@Param(value="branchIdList") List<String> branchIdList);

	@Query(value="select ref_no from dms_lead_stage_ref where stage_name = :stage and lead_id=:leadId",nativeQuery = true)
	String findRefByLeadIdStge(@Param(value="leadId") Integer leadId, 
								@Param(value="stage") String stage);

	@Query(value="SELECT * FROM dms_lead_stage_ref WHERE lead_id =:leadId",nativeQuery = true)
	List<LeadStageRefEntity> findLeadsByLeadId(@Param(value="leadId") int leadId);

	

}
