package com.automate.df.dao.dashboard;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.automate.df.entity.dashboard.DmsLead;

public interface DmsLeadDao extends JpaRepository<DmsLead, Integer> {

	
	@Query(value = "SELECT * FROM dms_lead where sales_consultant = :empName and createddatetime>=:startDate\r\n"
			+ "and createddatetime<=:endDate and lead_stage=:leadType", nativeQuery = true)
	List<DmsLead> getLeads(@Param(value = "empName") String empName,
			@Param(value = "startDate") String startDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "leadType") String leadType);
	
	@Query(value = "SELECT * FROM dms_lead where sales_consultant in(:empNamesList) and createddatetime>=:startDate\r\n"
			+ "and createddatetime<=:endDate and lead_stage=:leadType", nativeQuery = true)
	List<DmsLead> getAllEmployeeLeads(@Param(value = "empNamesList") List<String> empNamesList,
			@Param(value = "startDate") String startDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "leadType") String leadType);
	
	@Query(value = "SELECT count(*) FROM dms_lead where sales_consultant = :empName and createddatetime>=:startDate\r\n"
			+ "and createddatetime<=:endDate and lead_stage=:leadType", nativeQuery = true)
	Integer getLeadsCount(@Param(value = "empName") String empName,
			@Param(value = "startDate") String startDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "leadType") String leadType);
	
	

	@Query(value = "SELECT id FROM dms_lead where sales_consultant in(:empNamesList) ", nativeQuery = true)
	List<Integer> getLeadIdsByEmpNames(@Param(value = "empNamesList") List<String> empNamesList);
	
	
	// Vehicle model query starts here 
	@Query(value = "select distinct model from dms_lead", nativeQuery = true)
	List<String> getModelNames();
	
	
	
	@Query(value = "SELECT * FROM dms_lead where sales_consultant in(:empNamesList) and createddatetime>=:startDate\r\n"
			+ "and createddatetime<=:endDate and model=:model and organization_id=:orgId ", nativeQuery = true)
	List<DmsLead> getAllEmployeeLeadsWithModel(
			@Param(value = "orgId") String orgId,
			
			@Param(value = "empNamesList") List<String> empNamesList,
			@Param(value = "startDate") String startDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "model") String model);
	
	// Vehicle model query ends here
	
	
	// Lead Source and  EventSource query starts here
	
	@Query(value = "SELECT * FROM dms_lead where sales_consultant in(:empNamesList) and createddatetime>=:startDate\r\n"
			+ "and createddatetime<=:endDate and source_of_enquiry=:enqId and organization_id=:orgId", nativeQuery = true)
	List<DmsLead> getAllEmployeeLeadsBasedOnEnquiry(
			@Param(value = "orgId") String orgId,
		
			@Param(value = "empNamesList") List<String> empNamesList,
			@Param(value = "startDate") String startDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "enqId") Integer enqId);
	
	
	// Lead Source and  EventSource query ends here
	
	
	//Lost Drop query starts
	
	@Query(value = "SELECT * FROM dms_lead where sales_consultant in(:empNamesList) and createddatetime>=:startDate\r\n"
			+ "and createddatetime<=:endDate and model in (:model) and lead_stage=:leadType and organization_id=:orgId", nativeQuery = true)
	List<DmsLead> getAllEmployeeLeadsWithModelandStage(
			@Param(value = "orgId") String orgId,
			
			@Param(value = "empNamesList") List<String> empNamesList,
			@Param(value = "startDate") String startDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "model") List<String> model,
			@Param(value = "leadType") String leadType);
	
	

	@Query(value="SELECT * FROM dms_lead WHERE first_name=:firstName and last_name=:lastName", nativeQuery = true)
	List<DmsLead> verifyFirstName(@Param(value = "firstName") String firstName,@Param(value = "lastName") String lastName);

	
	@Query(value = "SELECT * FROM dms_lead where sales_consultant in(:empNamesList) and createddatetime>=:startDate\r\n"
			+ "and createddatetime<=:endDate and lead_stage in (:leadStages) and organization_id=:orgId", nativeQuery = true)
	List<DmsLead> getLeadsBasedonStage(@Param(value = "empNamesList") List<String> empNamesList,
			@Param(value = "startDate") String startDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "orgId") String orgId,
			@Param(value = "leadStages") List<String> leadStages);

	

	@Query(value = "SELECT * FROM dms_lead where sales_consultant in(:empNamesList) and createddatetime>=:startDate\r\n"
			+ "and createddatetime<=:endDate and lead_status=:leadType", nativeQuery = true)
	List<DmsLead> getAllEmployeeLeadsByLeadStatus(@Param(value = "empNamesList") List<String> empNamesList,
			@Param(value = "startDate") String startDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "leadType") String leadType);

	@Query(value = "SELECT * FROM dms_lead where sales_consultant in(:empNamesList) and createddatetime>=:startDate\r\n"
			+ "and createddatetime<=:endDate", nativeQuery = true)
	List<DmsLead> getAllEmployeeLeasForDate(@Param(value = "empNamesList") List<String> empNamesList,
			@Param(value = "startDate") String startDate,
			@Param(value = "endDate") String endDate);

	@Query(value = "SELECT * FROM dms_lead where id in(:idList)", nativeQuery = true)
	List<DmsLead> getLeadsBasedonId(@Param(value = "idList") List<Integer> idList);

	@Query(value = "SELECT id FROM dms_lead where sales_consultant in(:empNamesList) and createddatetime>=:startDate\r\n"
			+ "and createddatetime<=:endDate and organization_id=:orgId", nativeQuery = true)
	List<Integer> getLeadsBasedonEmpNames(@Param(value = "empNamesList") List<String> empNamesList,
			@Param(value = "startDate") String startDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "orgId") String orgId);
	//Lost Ddrop query ends
	
	
}
