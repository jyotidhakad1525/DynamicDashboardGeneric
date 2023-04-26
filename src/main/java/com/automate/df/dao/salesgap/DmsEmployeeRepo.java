package com.automate.df.dao.salesgap;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.automate.df.entity.salesgap.DmsEmployee;
import com.automate.df.entity.salesgap.TargetEntity;

public interface DmsEmployeeRepo extends JpaRepository<DmsEmployee, Integer> {
	
	@Query(value = "SELECT emp_name FROM dms_employee where emp_id in (:eidList)", nativeQuery = true)
	List<String> findEmpNamesById(@Param(value = "eidList") List<Integer> eidList);

	@Query(value = "SELECT * FROM dms_employee where org=:orgId and branch=:branchId", nativeQuery = true)
	List<DmsEmployee> getEmployeesByOrgBranch(@Param(value = "orgId") Integer orgId,
			@Param(value = "branchId") Integer branchId);
	
	
	@Query(value = "SELECT * FROM dms_employee where org=:orgId and status='Active'", nativeQuery = true)
	List<DmsEmployee> getEmployeesByOrg(@Param(value = "orgId") Integer orgId);
	
	
	@Query(value = "SELECT * FROM dms_employee where emp_id = :id", nativeQuery = true)
	Optional<DmsEmployee> findEmpById(@Param(value = "id") Integer id);
	

	@Query(value = "SELECT emp_id FROM dms_employee where emp_id in (:empNamelist)", nativeQuery = true)
	List<Integer> findEmpIdsByNames(@Param(value = "empNamelist") List<String> empNamelist);


	@Query(value = "SELECT emp_id FROM dms_employee where emp_name =:empName", nativeQuery = true)
	String findEmpIdByName(@Param(value = "empName") String empName);

	@Query(value = "SELECT * FROM dms_employee where reporting_to =:to", nativeQuery = true)
	Optional<DmsEmployee> findByReportingId(@Param(value = "to") String reportingTo);

}
