package com.automate.df.dao.oh;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.automate.df.entity.oh.LocationNodeData;

public interface LocationNodeDataDao extends JpaRepository<LocationNodeData, Integer>{

	@Query(value = "SELECT * FROM location_node_data where ref_parent_id=:refId and id in (:locationNodeIdList) and active='Y'", nativeQuery = true)
	List<LocationNodeData> getLocationNodeData(@Param(value = "refId") Integer refId,
			@Param(value = "locationNodeIdList") List<Integer> locationNodeIdList);
	
	@Query(value = "select distinct type FROM location_node_data where id in (:locationNodeIdList) and active='Y' order by type asc", nativeQuery = true)
	List<String> getEmpLevelData(
			@Param(value = "locationNodeIdList") List<Integer> locationNodeIdList);

	@Query(value = "SELECT * FROM location_node_data where type=:type and id in (:locationNodeIdList) and active='Y'", nativeQuery = true)
	List<LocationNodeData> getParentEmpDropdown(@Param(value = "type") String type,
			@Param(value = "locationNodeIdList") List<Integer> locationNodeIdList);
	
	@Query(value = "select * from location_node_data where org_id=:orgId and type=:levelType and code=:dropDownCode and active='Y'", nativeQuery = true)
	Optional<LocationNodeData> verifyLevelDataRecord(@Param(value = "orgId") Integer orgId,
			@Param(value = "levelType") String levelType,
			@Param(value = "dropDownCode") String dropDownCode);
	
	@Query(value = "select * from location_node_data where org_id=:orgId and type=:levelType and active='Y'", nativeQuery = true)
	List<LocationNodeData> getNodeDataByLevel(@Param(value = "orgId") Integer orgId,
			@Param(value = "levelType") String levelType);
	
	@Query(value = "select * from location_node_data where org_id=:orgId and type=:level and active='Y' and ref_parent_id in (:parentId)", nativeQuery = true)
	List<LocationNodeData> getNodeDataByParentId(@Param(value = "orgId") Integer orgId,
			@Param(value = "level") String level,
			@Param(value = "parentId") List<Integer> parentId);

	@Modifying
	@Transactional 
	@Query(value="update location_node_data set active='N' where org_id=:orgId and type=:levelcode",nativeQuery = true)
	void removeLevel(@Param(value = "orgId") Integer orgId, 
			@Param(value = "levelcode") String levelCodeToRemove);
	
	@Modifying
	@Transactional 
	@Query(value="update location_node_data set active ='N'  where active = 'Y' and id in (SELECT location_node_data_id FROM emp_location_mapping where emp_id=:empId and org_id=:orgId and active = 'Y')",nativeQuery = true)
	List<LocationNodeData> updateActiveNodeStatus(@Param(value = "orgId") Integer orgI,@Param(value = "empId") Integer empId);
	
	
	@Query(value="select * from location_node_data where active = 'Y' and id in (SELECT location_node_data_id FROM emp_location_mapping where emp_id=:empId and org_id=:orgId and active = 'Y') order by type asc\r\n"
			+ "",nativeQuery = true)
	List<LocationNodeData> getActiveLevelsForEmp(@Param(value = "orgId") Integer orgId,@Param(value = "empId") Integer empId);
	
	@Query(value="select * from location_node_data where active = 'Y'and org_id=:orgId order by type asc\r\n"
			+ "",nativeQuery = true)
	List<LocationNodeData> getActiveLevelsForOrg(@Param(value = "orgId") Integer orgId);
	
	
	@Modifying
	@Transactional 
	@Query(value="update location_node_data set active ='N'  where active = 'Y' and org_id = :orgId and id in (:idList)",nativeQuery = true)
	void removeOrgLevelNodes(@Param(value = "orgId") Integer orgId,@Param(value = "idList") List<Integer> idList);
	
	
	@Modifying
	@Transactional 
	@Query(value="update location_node_data set name = :name  where active = 'Y' and org_id = :orgId and id =:id",nativeQuery = true)
	void updateNodeDisplayName(@Param(value = "orgId") Integer orgId,@Param(value = "id") Integer id,
			@Param(value = "name") String name);
	
	
	@Query(value="select * from location_node_data where active = 'Y' and id in (SELECT location_node_data_id FROM emp_location_mapping where emp_id=:empId  and active = 'Y') order by type asc\r\n"
			+ "",nativeQuery = true)
	List<LocationNodeData> getActiveLevelsForEmpWithOutOrg(@Param(value = "empId") Integer empId);
	
	@Query(value="select type from location_node_data where id=:id and active = 'Y'",nativeQuery = true)
	String getLevelname(@Param(value = "id") Integer id);

	@Query(value="select leaf_node from location_node_data where id=:id and active = 'Y'",nativeQuery = true)
	String verifyLeafNode(@Param(value = "id") Integer id);

	

	
}
