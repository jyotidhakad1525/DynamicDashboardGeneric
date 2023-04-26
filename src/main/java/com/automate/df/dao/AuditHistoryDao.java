package com.automate.df.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.automate.df.entity.AuditHistory;

public interface AuditHistoryDao extends JpaRepository<AuditHistory, Integer> {

	@Query(value="SELECT * FROM audit_history where  UNIVERSAL_ID=:universalId and UPDATED_AT >= :startDate and UPDATED_AT<=:endDate ",nativeQuery = true)
	List<AuditHistory> getAuditDataById(
			@Param(value="universalId") String universalId,
			@Param(value="startDate") String startDate,
			@Param(value="endDate") String endDate);
	
	@Query(value="SELECT * FROM audit_history where lead_id = :leadId and UPDATED_AT >= :startDate and UPDATED_AT<=:endDate ",nativeQuery = true)
	List<AuditHistory> getAuditDataByIdLead(@Param(value="leadId") String leadId,
			
			@Param(value="startDate") String startDate,
			@Param(value="endDate") String endDate);
	
	@Query(value="SELECT * FROM audit_history where  UNIVERSAL_ID=:universalId and UPDATED_AT >= :startDate",nativeQuery = true)
	List<AuditHistory> getAuditDataByIdV2(
			@Param(value="universalId") String universalId,
			@Param(value="startDate") String startDate);
	
	@Query(value="SELECT * FROM audit_history where lead_id = :leadId and UPDATED_AT >= :startDate",nativeQuery = true)
	List<AuditHistory> getAuditDataByIdV2Lead(@Param(value="leadId") String leadId,
			@Param(value="startDate") String startDate);

	
	
	@Query(value="SELECT * FROM audit_history where STAGE_NAME = :stage or TASK_NAME=:task",nativeQuery = true)
	List<AuditHistory> getAuditDataByTaskStage(@Param(value="stage") String stageName,@Param(value="task") String task);

	@Query(value="SELECT * FROM audit_history where lead_id = :leadId or UNIVERSAL_ID=:universalId",nativeQuery = true)
	List<AuditHistory> getAuditDataByIdV3(@Param(value="leadId") String leadId,
			@Param(value="universalId") String universalId);
	
	


	
	@Query(value="SELECT * FROM audit_history where  UPDATED_AT >= :startDate and UPDATED_AT<=:endDate ",nativeQuery = true)
	List<AuditHistory> getAuditDataByIdV4(
			@Param(value="startDate") String startDate,
			@Param(value="endDate") String endDate);
	
	@Query(value="SELECT * FROM audit_history where  UPDATED_AT >= :startDate ",nativeQuery = true)
	List<AuditHistory> getAuditDataByIdV5(
			@Param(value="startDate") String startDate);
	

}
