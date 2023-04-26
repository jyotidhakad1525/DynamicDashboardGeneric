package com.automate.df.dao.oh;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.automate.df.entity.oh.EmpLocationMapping;

public interface EmpLocationMappingDao extends JpaRepository<EmpLocationMapping, Integer>{

	@Query(value = "SELECT location_node_data_id FROM emp_location_mapping where emp_id=:empId and org_id=:orgId and active='Y'", nativeQuery = true)
	List<Integer> getLeads(@Param(value = "orgId") Integer orgId,
			@Param(value = "empId") Integer empId);
	
	@Modifying
	@Transactional 
	@Query(value="update emp_location_mapping set active ='N' where emp_id=:empId and org_id=:orgId and active = 'Y'",nativeQuery = true)
	void updateActiveEmpMapStatus(@Param(value = "orgId") Integer orgI,@Param(value = "empId") Integer empId);
	
	@Query(value = "SELECT * FROM emp_location_mapping where emp_id=:empId and org_id=:orgId and active='Y' and location_node_data_id in (:nodeList)", nativeQuery = true)
	List<EmpLocationMapping> getSelectedMappingsForEmp(@Param(value = "orgId") Integer orgId,
			@Param(value = "empId") Integer empId,
			@Param(value="nodeList") List<Integer> nodeList);
	
	@Modifying
	@Transactional 
	@Query(value="update emp_location_mapping set active='N' where location_node_data_id in(\r\n"
			+ "select id from location_node_data where org_id=:orgId  and location_node_def_id in (select id from location_node_def where org_id = :orgId and location_node_def_type in (:levelList)))\r\n"
			+ "",nativeQuery = true)
	void removeActiveMappings(@Param(value = "orgId") Integer orgId,
			@Param(value="levelList") List<String> levelList);

	@Query(value = "SELECT location_node_data_id FROM emp_location_mapping where emp_id in(:eidList) and active='Y'", nativeQuery = true)
	Set<Integer> findMappingsByEmpId(@Param(value="eidList") List<Integer> eidList );

}
